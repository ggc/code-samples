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


var t = 0;
// Matlab json parsing from file
var tempMatrix = {};
var irradiationMatrix = {};
var tempPredMatrix = {};
var irradiationPredMatrix = {};

var dayIrradiation = {};
var dayTemperature = {};
var dayIrradiationPred = {};
var dayTemperaturePred = {};

var url = 'mongodb://localhost/weather';

var fillValues = function(){
    for (let d in irradiationMatrix){
        for (let t = 0; t<24; t++){
            if( irradiationMatrix[''+d][''+t] === undefined){
                console.log('day '+d+' hour '+t+' its empty');
                t === 0 ? irradiationMatrix[''+d][''+t] = 0 : irradiationMatrix[''+d][''+t] = irradiationMatrix[''+d][''+(t-1)];
            }
            if( irradiationPredMatrix[''+d][''+t] === undefined){
                console.log('day '+d+' hour '+t+' its empty');
                t === 0 ? irradiationPredMatrix[''+d][''+t] = 0 : irradiationPredMatrix[''+d][''+t] = irradiationPredMatrix[''+d][''+(t-1)];
            }
            if( tempMatrix[''+d][''+t] === undefined){
                console.log('day '+d+' hour '+t+' its empty');
                t === 0 ? tempMatrix[''+d][''+t] = 0 : tempMatrix[''+d][''+t] = tempMatrix[''+d][''+(t-1)];
            }
            if( tempPredMatrix[''+d][''+t] === undefined){
                console.log('day '+d+' hour '+t+' its empty');
                t === 0 ? tempPredMatrix[''+d][''+t] = 0 : tempPredMatrix[''+d][''+t] = tempPredMatrix[''+d][''+(t-1)];
            }
        }
    }
}

var findPreds = function(db, callback) {
    var collection = db.collection('preds');

    var cursor = collection.find({$and: [
            {"prediction_made": 
                {$gte: new Date("2017-03-02")}
            },
            {"prediction_made": 
                {$lt: new Date("2017-04-03")}
            }
        ]      
    });

    // Every doc has Current hour info and Next 24 hours info
    cursor.each((err, doc) => {
        if (err) throw err;
        if(doc){
            let date = ''+doc.prediction_made.getUTCFullYear()+'-'+(doc.prediction_made.getUTCMonth())+'-'+doc.prediction_made.getUTCDate();
            let time = doc.prediction_made.getUTCHours();

            if( !irradiationMatrix[date] ) irradiationMatrix[date] = {}
            if( !tempMatrix[date] ) tempMatrix[date] = {};

            // Take prediction [00-23] at start of the day
            if(time  == 0){
                if( !irradiationPredMatrix[date] ) irradiationPredMatrix[date] = {}
                if( !tempPredMatrix[date] ) tempPredMatrix[date] = {};
                for (let d in doc.response.hourly.data) {
                    irradiationPredMatrix[date][d] = doc.response.hourly.data[d].cloudCover;
                    tempPredMatrix[date][d] = doc.response.hourly.data[d].temperature;
                }
            }

            irradiationMatrix[date][time] = doc.response.currently.cloudCover;
            tempMatrix[date][time] = doc.response.currently.temperature;
            t++;
        }
        else{
            fillValues();
            // console.log(irradiationMatrix);
            callback();
        }
    });
};

mongo.connect(url, (err, db) => {
    if(err) throw err;
    console.log('Connected to db weather');

    findPreds(db, () => {
        console.log(t+' docs retrieved')
        // console.log('Temperatures', tempMatrix);
        let fd_ir = fs.openSync('assets/ir.txt','w');
        let fd_irpred = fs.openSync('assets/ir_pred.txt','w');
        let fd_temp = fs.openSync('assets/temp.txt','w');
        let fd_temppred = fs.openSync('assets/temp_pred.txt','w');

        fillValues();

        fs.write(fd_ir, JSON.stringify(irradiationMatrix), (err, written, string) => {
          // console.log('Irradiation written');
        })
        fs.write(fd_irpred, JSON.stringify(irradiationPredMatrix), (err, written, string) => {
          // console.log('Irradiation pred written');
        })
        fs.write(fd_temp, JSON.stringify(tempMatrix), (err, written, string) => {
          // console.log('Temperature written');
        })
        fs.write(fd_temppred, JSON.stringify(tempPredMatrix), (err, written, string) => {
          // console.log('Temperature pred written');
        })
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
