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
    userReviews: [UserReviewSchema],
    loginToken:String
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
// sends the confirmation email to the email address provided
UserModel.confirmAccount = function (email,res) {
    let confirmationCode = Math.floor((Math.random()*100000)+10000).toString();

    UserModel.findOneAndUpdate({email:email},{$set:{confirmationCode:confirmationCode}},(err,doc)=>{
        if(err)
        {
            res.send('server error'); //means a server error
        }
    });
    let transporter = nodeMailer.createTransport({
        service:'gmail',
        auth: {
            user:'businesscommunicationplatform@gmail.com',
            pass:'havocHavoc123#'
        }
    });
    let mailOptions = {
        from: '"BizCom " <businesscommunicationplatform@gmail.com>',
        to: email,
        subject: 'BizCom: Signup Confirmation',
        text: 'Your confirmation code is '+confirmationCode,
    };
    transporter.sendMail(mailOptions,(err,info)=>{
        if(err)
        {
            console.log(err);
            res.send("error sending email");
        }
        else
        {
            res.send("email sent");
        }
    });
};
module.exports = UserModel;

