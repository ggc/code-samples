var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var mongo = require('mongodb').MongoClient;

var routes = require('./routes/index');
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

app.use('/', routes);
app.use('/users', users);

// DB handler
// mongoose.connect('mongodb://localhost/weather');
// var db = mongoose.connection;
// db.once('open', () => {
//   console.log('Connected to db');
// })
var readyToParse;
// Matlab json parsing from file
var solarValues = [];

var url = 'mongodb://localhost/weather';

var cursorIterator = function(cursor, callback) {
    cursor.hasNext((err, result) => {
        if(err) {
            callback(err);
        }
        cursor.skip(1).limit(24)
        .toArray((err, docs) => {
            if(err) throw err;
            console.log('I found '+docs.length()+' items: ', docs)

            solarValues.push({
              'temperature': docs[0].response.currently.temperature,
              'cloudCover': docs[0].response.currently.cloudCover
            })
            callback(docs);
        });
        cursorIterator(cursor, callback);
    })
};


var findPreds = function(db, callback) {
    var collection = db.collection('preds');

    var cursor = collection.find({});
    t = 0;
    day = [];
    cursor.each((err, doc) => {
        // console.log('doc: ', doc)
        if(doc){
            day.push(doc.response.currently.temperature);
            if(doc.prediction_made.getHours() == 0){
              solarValues.push(day);
              day = [];
              t++;
            }
            // solarValues
            // .push({
            //     'temperature': doc.response.currently.temperature,
            //     'cloudCover': doc.response.currently.cloudCover
            // })
        }
        else{
          cursor.close();
          callback();
        }
    })    
};

mongo.connect(url, (err, db) => {
    if(err) throw err;
    console.log('Connected to db weather');

    // Remember to use db.close();
    findPreds(db, () => {


      console.log('Solar values......', solarValues);
      db.close();
    })
})



















// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
