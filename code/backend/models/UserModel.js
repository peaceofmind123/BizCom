const mongoose = require('mongoose');
const Schema = mongoose.Schema;

let UserSchema = new Schema({
    fName: String,
    lName: String,
    userName: String,
    password: String,
    phone: String,
    city: String,
    country: String,
    userType: String,

}, {collection: 'users'});

let UserModel = mongoose.model('UserModel',UserSchema);

module.exports = UserModel;

