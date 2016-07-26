(function () {
	angular
	.module('loc8rApp')
	.directive('ratingStars', ratingStars);

	// Important: name function in camelCase. In HTML -> rating-stars
	function ratingStars () {
		return {
			restrict: 'EA', // TODO What is this?
			scope: {
				thisRating: '=rating'
			},
			templateUrl: 'ratingStars.template.html' // TODO absolute/relative path?
		};
	}
}) ();