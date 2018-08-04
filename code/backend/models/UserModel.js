const mongoose = require('mongoose');
const Schema = mongoose.Schema;
let UserReviewSchema = new Schema({
    userName:String,
    rating:Number
});
let UserSchema = new Schema({
    fName: String,
    lName: String,
    userName: String,
    password: String,
    phone: String,
    email:String,
    city: String,
    country: String,
    userType: String,
    isUserRegistered:{type:Boolean,default:false},
    confirmationCode: String,
    forgotPasswordCode:String,
    passwordChangeRequest:{type:Boolean,default:false},
    profilePicPath:String,
    mainAdPicPath:String,
    isLoggedIn: {type:Boolean, default:false},
    score:Number,
    additionalInfo:String,
    additionalAdPicsPath:[String],
    userReviews: [UserReviewSchema]
}, {collection: 'users'});
UserSchema.index(
    {
        fName:'text',
        lName:'text',
        userName:'text',
        city:'text',
        country:'text',

    });
let UserModel = mongoose.model('UserModel',UserSchema);

module.exports = UserModel;

