var express = require('express');
var router = express.Router();
var request = require('request');

/* GET home page. */
router.get('/', function(req, response, next) {
	// TODO: Take out api token
	options = {
		uri: 'http://dashboard.situm.es:80/api/v1/buildings/833/real_time',
		headers: {
			'Content-Type': 'application/json',
			'X-API-EMAIL': 'gabriel.galan.17@gmail.com',
			'X-API-KEY': '7FZYUAgZqHY2d1kVKq0L5Hl46Hdx2Liw'		
		}
	};
	var coords;
	request(options, function(err, res, body) {
		var jsonResponse = JSON.parse(body);
		coords = jsonResponse.features[0].geometry.coordinates;
		if(res.statusCode == 200){
			response.render('index', { title: 'Express', lat: coords[0], lng: coords[1]});
		}
	})
});

module.exports = router;
