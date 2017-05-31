var login = require('./login.js');
var signup = require('./signup.js');
var User = require('../models/user.js');

module.exports = function ( passport ) {
	// Login and Logout
	// ASK: What if this is not here?
	passport.serializeUser(function(user, done) {
		done(null, user._id);
	});

	passport.deserializeUser(function(id, done) {
		User.findById(id, function(err, user) {
			done(err, user);
		});
	});

	login(passport);
	signup(passport);
}