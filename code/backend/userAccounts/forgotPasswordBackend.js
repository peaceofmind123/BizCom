const express = require('express');
const forgotPasswordBackend = express.Router();
const UserModel = require('../models/UserModel');
const nodeMailer = require('nodemailer');

forgotPasswordBackend.route('/sendForgotCode').post((req,res)=>{
    let forgotPasswordCode = Math.floor((Math.random()*100000)+10000).toString(); //a 5-6 digit random code
    UserModel.findOneAndUpdate({email:req.body.email},{$set:{forgotPasswordCode:forgotPasswordCode}},(err,doc)=>{
        if(err)
            res.send("email not found");

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
        to: req.body.email,
        subject: 'BizCom: Reset Password',
        text: 'Your password reset code is: '+forgotPasswordCode,
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


});
/*todo: create forgot password check code route*/

module.exports = forgotPasswordBackend;