var mongoose = require('mongoose');

var reviewSchema = new mongoose.Schema({
	// Third and last barrir of integrity check
    author: {type: String, required: true},
    rating: {
        type: Number,
        required: true,
        min: 0,
        max: 5
    },
    reviewText: { type: String, required: true},
    createdOn: {
        type: Date,
        "default": Date.now
    }
});

var openingTimeSchema = new mongoose.Schema({
    days: {
        type: String,
        required: true
    },
    opening: String,
    closing: String,
    closed: {
        type: Boolean,
        required: true
    }
});

var locationSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    address: String,
    rating: {
        type: Number,
        "default": 0,
        min: 0,
        max: 5
    },
    facilities: [String],
    // Always store coordinates longitude, latitude order.
	// Change 2dsphere index to version 1 or 2.
	//   dropIndex("coords_2dsphere")
	//   createIndex({coords:"2dsphere"})
    coords: {
        type: [Number],
        index: '2dsphere',
		required: true
    },
    openingTimes: [openingTimeSchema],
    reviews: [reviewSchema]
});

mongoose.model('Location', locationSchema);
