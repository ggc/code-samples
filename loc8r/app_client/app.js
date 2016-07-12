angular.module('loc8rApp', ['ngRoute']);

// Follow ng-app 'loc8r' in layout.jade to get here.

function config ($routeProvider) {
	$routeProvider
	// config route definitions
	.when('/', {
		templateUrl: 'home.view.html', // Paste this 'url-to-template' on 'content'
		controller: 'homeCtrl' // "Use this controller"
	})
	.otherwise({redirectTo: '/'});
}

angular
.module('loc8rApp')
.config(['$routeProvider', config]); // Notifiy module about config func