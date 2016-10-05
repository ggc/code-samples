// server.js

var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server); // Crea un websocket conectado al servidor

app.use( express.static(__dirname + '/bower_components') ); // Para usar componentes de bower (i.e. jquery) facilmente

io.on('connection', function(client) { // Ante una conexion de "client"...
	console.log('Client connected...');

	client.on('join', function(data) {
		console.log(data);
	});

	client.on('messages', function(data) {
		client.emit('broad', data);
		client.broadcast.emit('broad', data);
	});
});


app.get( '/', function(req, res, next ) {
	res.sendFile(__dirname + '/index.html');
});

server.listen(3000);