package com.example.deep;

import android.view.View;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 지웅 on 2017-02-03.
 */
public class chooseActivity extends Activity {
    Bitmap bmp;
    byte barray[];
    Client client;
    String answer;
    TextView txt;
    String s2[] = new String[30];
    int n = 0;
    @Override
    public void onBackPressed(){
        finish();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_layout);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            barray = extras.getByteArray("bmpImage");
            bmp = BitmapFactory.decodeByteArray(barray, 0, barray.length);
            answer = extras.getString("ANS");
        }
        s2 = answer.split(" ");
        setImage(n);
        Button moreButton = (Button)findViewById(R.id.more);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(n+2 > 5)
                    n = 0;
                else
                    n+=2;
                setImage(n);
            }
        });
        Button returnButton = (Button)findViewById(R.id.back);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSubActivity =
                        new Intent(chooseActivity.this, MainActivity.class);
                startActivity(intentSubActivity);
                finish();
            }
        });

        ImageButton Button1 = (ImageButton)findViewById(R.id.View1);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "LEFT!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton Button2 = (ImageButton)findViewById(R.id.View2);
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "RIGHT!", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(), "알고싶은 물체를 클릭 해 주세요!", Toast.LENGTH_SHORT).show();
    }

    void setImage(int q){
        int id = getid(s2[q]);
        ImageView view = (ImageView)findViewById(R.id.View1);
        view.setImageResource(id);
        int id2 = getid(s2[q+1]);
        ImageView view2 = (ImageView)findViewById(R.id.View2);
        view2.setImageResource(id2);
    }
    int getid(String s){
        if (s.equals("bag"))
            return R.drawable.bag;
        if (s.equals("bottle"))
            return R.drawable.bottle;
        if (s.equals("can"))
            return R.drawable.can;
        if (s.equals("cd"))
            return R.drawable.cd;
        if (s.equals("chair"))
            return R.drawable.chair;
        if (s.equals("coat"))
            return R.drawable.coat;
        if (s.equals("cup"))
            return R.drawable.cup;
        if (s.equals("earphone"))
            return R.drawable.earphone;
        if (s.equals("eyeglass"))
            return R.drawable.eyeglass;
        if (s.equals("gloves"))
            return R.drawable.gloves;
        if (s.equals("drier"))
            return R.drawable.hairdrier;
        if (s.equals("handbag"))
            return R.drawable.handbag;
        if (s.equals("handcream"))
            return R.drawable.handcream;
        if (s.equals("laptop"))
            return R.drawable.laptop;
        if (s.equals("lipstick"))
            return R.drawable.lipstick;
        if (s.equals("monitor"))
            return R.drawable.monitor;
        if (s.equals("mouse"))
            return R.drawable.mouse;
        if (s.equals("nailclipper"))
            return R.drawable.nailclipper;
        if (s.equals("pen"))
            return R.drawable.pen;
        if (s.equals("ring"))
            return R.drawable.ring;
        if (s.equals("sandle"))
            return R.drawable.sandle;
        if (s.equals("scissor"))
            return R.drawable.scissor;
        if (s.equals("shoes"))
            return R.drawable.shoes;
        if (s.equals("slipper"))
            return R.drawable.slipper;
        if (s.equals("phone"))
            return R.drawable.smartphone;
        if (s.equals("socks"))
            return R.drawable.socks;
        if (s.equals("spoon"))
            return R.drawable.spoon;
        if (s.equals("umbrella"))
            return R.drawable.umbrella;
        if (s.equals("wallet"))
            return R.drawable.wallet;
        if (s.equals("watch"))
            return R.drawable.watch;
        else
            return R.drawable.white;
    }
}
