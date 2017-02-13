// Server code for the object recognition project
// To execute, simply type node server.js in the terminal
var net = require('net');
var fs = require('fs');
var sockets = [];

var svr = net.createServer(function(sock) {
    console.log('Connected: ' + sock.remoteAddress + ':' + sock.remotePort);
    sockets.push(sock);
    // Create a buffer which will contain every single packets from the client
    var packets = new Buffer(0);
    var flag = 0;
    var chksum = 0;
    sock.on('data', function(chunk) {
        var newBuf = new Buffer(chunk); 
	// Accumulate packet in the buffer
	packets = Buffer.concat([packets, newBuf]);
	// First, receive the total length of the packet and save it as checksum
	if(flag == 0){	
		chksum = chunk.readIntBE(0, 4);
		flag = 1;
		console.log(chksum);
	}
	// console.log('%d', packets.length);
	// To know if we reached the end of packet.
	if(chksum + 4 == packets.length){
		console.log("Received all packets");
		// Since there are 4 extra bytes at the front of buffer, remove them.
		packets = packets.slice(4, packets.length);
		fs.writeFile('./uploads/photo.jpg', packets, 'binary', function(err) {
      			if(err) {
       				console.log(err);
       			} else {
       				console.log("Picture saved in uploads folder");
       			}
		});

		// Feed the saved picture to the deep learning engine 
		// We create a python subprocess to do this.
		var spawn = require("child_process").spawn;
		var process = spawn('python',["Classify.py", "photo.jpg"]);
		process.stdout.on('data', function (data){
			console.log('%s', data);
			try{
				sock.write(data+'\n');
			}		
			catch(exception){
				console.log(exception);
			}
		});				
	}
    });

    // End of connection. delete all of the created sockets and exit
    sock.on('end', function() {
	console.log("Connetion Lost");
	var idx = sockets.indexOf(sock);
	if(idx != -1)
		delete sockets[idx];
    });
    
});

var svraddr = '203.252.121.225';
var svrport = 3000;
 
svr.listen(svrport, svraddr);
console.log('Server Created at ' + svraddr + ':' + svrport + '\n');
