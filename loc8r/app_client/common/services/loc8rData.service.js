(function () {
	angular
	.module('loc8rApp')
	.service('loc8rData', loc8rData);

	loc8rData.$inject = ['$http']; // Necessary to minify js files
	// This is a service
	function loc8rData ($http) {
		var locationByCoords = function (lat, lng) {
			//console.log("lat: " + lat + ", lng: " + lng); // Why this kind of logs doen't show?
			return $http.get('/api/locations?lng=' + lng + '&lat=' + lat + '&maxDistance=500');
		};
		return {
			locationByCoords: locationByCoords
		};
	}
}) ();