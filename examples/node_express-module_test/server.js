var express = require('express'),
	session = require('express-session'),
	cookieParser = require('cookie-parser'),
	i18n = require('i18n'),
	lessMiddleware = require('less-middleware');

var app = express();

app.use(lessMiddleware(
		__dirname + 'public/style/less',
		{
			dest: __dirname + 'public/style/css' ,
			compress: 'auto'
		}
	));
app.use(express.static(__dirname + '/public'));

i18n.configure({

	//define how many languages we would support in our application
	locales:['en', 'es'],

	//define the path to language json files, default is /locales
	directory: __dirname + '/locales',

	//define the default language
	defaultLocale: 'es',

	// define a custom cookie name to parse locale settings from 
	cookie: 'i18n'
});

app.use(cookieParser("node-express-i18n"));
app.use(session({
	secret: "node-express-i18n",
	resave: true,
	saveUninitialized: true,
	cookie: { maxAge: 60000 }
}));
app.use(i18n.init);

app.set('view engine', 'jade'); // Default option
app.listen(3000);

app.get('/', function (req, res) {
    res.setLocale(req.cookies.i18n);
    res.render('main', {
    i18n: res
    })
});

app.get('/contact', function (req, res) {
    res.render('contact', {
    i18n: res
    })
});

app.get('/en', function (req, res) {
    res.cookie('i18n', 'en');
    res.redirect('/')
});

app.get('/es', function (req, res) {
    res.cookie('i18n', 'es');
    res.redirect('/')
});