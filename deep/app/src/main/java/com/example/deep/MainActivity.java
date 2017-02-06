package com.example.deep;
// USED OPEN-SOURCE
// OpenCV for android 2410
// ION library
// Node JS & Express

import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.android.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import java.math.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.lang.Object;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements CvCameraViewListener2  {

    private static final String    TAG = "Yklab";

    Client client;

    private CameraBridgeViewBase   mOpenCvCameraView;

    private Socket socket;

    private AlertDialog confirmDialog;
    Mat mRgba, cpyMat, showMat;
    boolean takePic = false, dragEvent = false;
    int p1_x = 0, p1_y = 0, p2_x = 0, p2_y = 0;
    Bitmap bmp;
    byte barray [];
    String msg;


    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

    }

    public void onCameraViewStopped() {

    }

    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        if(dragEvent){
            // Get the x and y coordinates of the touch point
            int x1 = (int) event.getX();
            int y1 = (int) event.getY();
            // On the first touch
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                p1_x = x1; p1_y = y1; p2_x = x1; p2_y = y1;
            }

            // From the continuing touch
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                p2_x = x1; p2_y = y1;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if(Math.abs(p1_x-p2_x) * Math.abs(p1_y-p2_y) > 10){
                    Rect setRect = new Rect( Math.min(p1_x, p2_x), Math.min(p1_y, p2_y), Math.abs(p1_x-p2_x), Math.abs(p1_y - p2_y));
                    Mat area = cpyMat.submat(setRect);
                    Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                    bmp = Bitmap.createBitmap(area.width(), area.height(), conf);
                    Utils.matToBitmap(area, bmp);
                    confirmDialog = createConfirmDialog();
                    confirmDialog.show();
                }
            }
        }
        return true;
    }
    // Camera Function
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        p1_x = 0; p1_y = 0; p2_x = 0; p2_y = 0;
                        cpyMat = mRgba.clone();
                        takePic = !takePic;
                        dragEvent = !dragEvent;
                    }
                }
        );
        System.gc();
        if(takePic) {
            showMat = cpyMat.clone();
            if(Math.abs(p1_x-p2_x) * Math.abs(p1_y-p2_y) > 10)
                Core.rectangle(showMat, new Point(p1_x, p1_y), new Point(p2_x, p2_y), new Scalar(255, 0, 0), 3);
            return showMat;
        }
        else
            return mRgba;
    }

    private AlertDialog createConfirmDialog(){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Is this the image?");

        ImageView view = new ImageView(this);
        view.setImageBitmap(bmp);
        ab.setView(view);

        // Initialize the positive button
        ab.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),true);
                bmp = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
                ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, bmpStream);
                barray = bmpStream.toByteArray();
                client = new Client("192.168.0.18", 3000);
                client.setClientCallback(new Client.ClientCallback () {
                    @Override
                    public void onMessage(String message) {
                        if(!message.equals(""))
                            msg = message;
                        client.disconnect();
                        Intent intentSubActivity =
                                new Intent(MainActivity.this, chooseActivity.class);
                        intentSubActivity.putExtra("bmpImage", barray);
                        intentSubActivity.putExtra("ANS", msg);
                        startActivity(intentSubActivity);
                    }

                    @Override
                    public void onConnect(Socket socket) {
                        client.send(barray);
                        client.send("");
                        //client.disconnect();
                    }

                    @Override
                    public void onDisconnect(Socket socket, String message) {
                    }

                    @Override
                    public void onConnectError(Socket socket, String message) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Can not connect to server", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                client.connect();

            }
        });

        // Initialize the negative button
        ab.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return ab.create();
    }
    private AlertDialog createErrorDialog(String e) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(e);
        return ab.create();
    }
}
