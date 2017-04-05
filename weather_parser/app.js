var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var fs = require('fs');
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
var tempMatrix = [];
var irradiationMatrix = [];
var tempPredMatrix = [];
var irradiationPredMatrix = [];

var url = 'mongodb://localhost/weather';

// var cursorIterator = function(cursor, callback) {
//     cursor.hasNext((err, result) => {
//         if(err) {
//             callback(err);
//         }
//         cursor.skip(1).limit(24)
//         .toArray((err, docs) => {
//             if(err) throw err;
//             console.log('I found '+docs.length()+' items: ', docs)

//             irradiationMatrix.push({
//               'temperature': docs[0].response.currently.temperature,
//               'cloudCover': docs[0].response.currently.cloudCover
//             })
//             callback(docs);
//         });
//         cursorIterator(cursor, callback);
//     })
// };


var findPreds = function(db, callback) {
    var collection = db.collection('preds');

    var cursor = collection.find({"prediction_made": {$gte: new Date("2017-03-00")}}); // {"prediction_made": {$gt: new ISODate("2017-03-21")}}
    t = 0;
    elementsAdded = 0;
    dayIrradiation = {};
    dayTemperature = [];
    dayIrradiationPred = [];
    dayTemperaturePred = [];

    // For every document retrieved
    cursor.each((err, doc) => {
        if(doc){
            // Init row with attr named with date
            let date = ''+doc.prediction_made.getUTCFullYear()+'-'+(doc.prediction_made.getMonth()+1)+'-'+(doc.prediction_made.getDate()+1);
            if( !dayIrradiation[date] ){
                dayIrradiation[date] = [];
                console.log('lest get empty at ',date)
            }

            // Add current values to 'day' vector
            // dayIrradiation[date].push(doc.response.currently.cloudCover);
            dayIrradiation[date][doc.prediction_made.getHours()+1] = doc.response.currently.cloudCover;
            dayTemperature.push(doc.response.currently.temperature);
            elementsAdded++;

            // At start of every day...
            if(doc.prediction_made.getHours() == 23){
                // Create pred day
                for (let d in doc.response.hourly.data) {
                    dayIrradiationPred.push(doc.response.hourly.data[d].cloudCover);
                    dayTemperaturePred.push(doc.response.hourly.data[d].temperature);
                }
                
                irradiationPredMatrix.push(dayIrradiationPred);
                tempPredMatrix.push(dayTemperaturePred);

                // Add completed last day to matrix
                irradiationMatrix.push(dayIrradiation);
                tempMatrix.push(dayTemperature);

                console.log('added '+dayIrradiation[date].length+' '+elementsAdded+' elements at day '+t);
                console.log('Date: ', doc.prediction_made.toString());
                dayIrradiationPred = [], 
                dayTemperaturePred = [], 
                // dayIrradiation = {}, 
                dayTemperature = [];
                t++;
            }
        }
        else{
          cursor.close();
          console.log('calling callback');
          callback();
        }
    })    
};

mongo.connect(url, (err, db) => {
    if(err) throw err;
    console.log('Connected to db weather');

    findPreds(db, () => {
      // console.log('Temperatures', tempMatrix);
      let fd_ir = fs.openSync('ir.txt','w');
      let fd_irpred = fs.openSync('ir_pred.txt','w');
      let fd_temp = fs.openSync('temp.txt','w');
      let fd_temppred = fs.openSync('temp_pred.txt','w');

      fs.write(fd_ir, JSON.stringify(dayIrradiation), (err, written, string) => {
        console.log('Irradiation written');
      })
      fs.write(fd_irpred, irradiationPredMatrix, (err, written, string) => {
        console.log('Irradiation pred written');
      })
      fs.write(fd_temp, tempMatrix, (err, written, string) => {
        console.log('Temperature written');
      })
      fs.write(fd_temppred, tempPredMatrix, (err, written, string) => {
        console.log('Temperature pred written');
      })
      // console.log('Irradiations', irradiationMatrix);
      // console.log('Irradiations pred', irradiationPredMatrix);
      // console.log('Temperatures pred', tempPredMatrix);
      // console.log('Retrieved '+t+' days');
      fs.closeSync(fd_ir);
      fs.closeSync(fd_irpred);
      fs.closeSync(fd_temp);
      fs.closeSync(fd_temppred);
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
