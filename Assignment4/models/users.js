var mongoose = require('mongoose');
var passportLocalMongoose = require('passport-local-mongoose');
var Schema = mongoose.Schema;

let User = new Schema({
    admin: {
        type: Boolean,
        default: false
    },
    firstname: {
        type: String,
        required: true
    },
    lastname: {
        type: String,
        required: true
    },
    username: {
        type: String,
        required: true
    },
    password: {
        type: String,
        required: true
}  
});

User.plugin(passportLocalMongoose);

module.exports = mongoose.model('User', User);