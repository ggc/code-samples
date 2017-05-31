var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var request = require('request');

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/users', users);

var options = { 
	method: 'POST',
	url: 'http://172.18.0.4:1026/NGSI10/queryContext',
	rejectUnauthorized: false,
	headers: 
	{ 
		'cache-control': 'no-cache',
		'fiware-servicepath': '/SmartParking',
		'fiware-service': 'sc_thinkingcity',
		'accept': 'application/json',
		'content-type': 'application/json' 
	},
	body: { 	
		entities: [ { 
			type: 'Persona', 
			isPattern: 'true', 
			id: '.*' 
		} ],
		restriction: { 
			scopes: [ { 
				type: 'FIWARE::StringQuery', 
				value: 'Email==gabriel.galancasillas@telefonica.com'
			} ] 
		}
	},
	json: true 
};

request(options, function (error, response, body) {
	console(error);
	console(response);
});




// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
