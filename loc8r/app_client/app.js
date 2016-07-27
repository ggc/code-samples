(function () {
	angular.module('loc8rApp', ['ngRoute', 'ngSanitize']);

	// Follow ng-app 'loc8r' in layout.jade to get here.

	function config ($routeProvider, $locationProvider) {
		$routeProvider
		// config route definitions
		.when('/', {
			templateUrl: 'home/home.view.html', // Paste this 'url-to-template' on 'content'
			controller: 'homeCtrl', // "Use this controller"
			controllerAs: 'vm' // (ViewModel) Use controllerAs to not keep $SCOPE overload
		})
		.when('/about', {
			templateUrl: '/common/views/genericText.view.html', // Paste this 'url-to-template' on 'content'
			controller: 'aboutCtrl', // "Use this controller"
			controllerAs: 'vm' // (ViewModel) Use controllerAs to not keep $SCOPE overload
		})
		.otherwise({redirectTo: '/'});

		$locationProvider.html5Mode(true);
	}

	angular
	.module('loc8rApp')
	.config(['$routeProvider', '$locationProvider', config]); // Notifiy module about config func
}) ();