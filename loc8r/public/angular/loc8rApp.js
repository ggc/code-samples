angular.module('loc8rApp', []);

var locationListCtrl = function ($scope) {
	$scope.data = {
		locations: [{
			name : 'Starfucks',
			address : '125 Real street',
			rating : 4,
			facilities : ['Hot drinks','Food','Premium wifi'],
			distance: '0.296456',
			_id: '57656b0f97794b7259511bc9'
		},{
			name : 'Coffee Shop',
			address : '125 Real street',
			rating : 4,
			facilities : ['Cool','Nice people'],
			distance: '0.289455',
			_id: '57672bfdf4234887924c027a'
		}]
	};
};

angular
.module('loc8rApp')
.controller('locationListCtrl', locationListCtrl);