var mongoose = require('mongoose');
var passportLocalMongoose = require('passport-local-mongoose');
var Schema = mongoose.Schema;

let ItemSchema = new Schema({
    itemName: {
        type: String,
        default: true
    },
    itemPrice: {
        type: String,
        required: true
    },
    itemImageName: {
        type: String,
        default: ''
    },
    itemDescription: {
        type: String,
        required: true
    }
},
    {
    timestamps: true
    });

var Items = mongoose.model('Item', ItemSchema);
module.exports = Items;