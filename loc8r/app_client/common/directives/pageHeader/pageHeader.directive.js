(function () {

	angular
	.module('loc8rApp')
	.directive('pageHeader', pageHeader);

	function pageHeader () {
		// To use this isolated scope:
		//     <page-header content="naninani"></page-header>
		return {
			restrict: 'EA',
			scope: {
				content: '=content'
			},
			templateUrl: '/common/directives/pageHeader/pageHeader.template.html'
		};
	}
}) ();