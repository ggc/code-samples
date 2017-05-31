var express = require('express');
var fs = require('fs');
var https = require('https');

var app = express();

//Certificates (autosignatured) for https
const options = {
    key: fs.readFileSync('cert/server_selfsigned.key'),
    cert: fs.readFileSync('cert/server_selfsigned.crt'),
    requestCert: false,
    rejectUnauthorized: false
};


app.set('port', 443)
var server = https.createServer(options, app);
var io = require('socket.io')(server); // Crea un websocket conectado al servidor

server.listen(app.get('port'), function() {
    console.log('Server listening on ',app.get('port'))
});

app.use( express.static(__dirname + '/bower_components') ); // Para usar componentes de bower (i.e. jquery) facilmente

io.on('connection', function(client) { // Ante una conexion de "client"...
	console.log('Client connected...');

	client.on('join', function(data) {
		console.log(data);
	});

	client.on('messages', function(data) {
		client.emit('broad', data);
		client.broadcast.emit('broad', data);
        console.log(data);
	});
});


app.get( '/', function(req, res, next ) {
	res.sendFile(__dirname + '/index.html');
});
