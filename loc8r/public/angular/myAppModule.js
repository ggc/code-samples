var controllerFunction = function($scope) {
	$scope.myInput = "world!";
}

angular
	.module('myApp', [])
	.controller('myAppController', controllerFunction);