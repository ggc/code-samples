var express = require('express');
var app = express();
var server = require('http').createServer(app);

app.get( '/', function(req, res, next) {
	res.sendFile(__dirname + '/index.html');
});

server.listen(3000);