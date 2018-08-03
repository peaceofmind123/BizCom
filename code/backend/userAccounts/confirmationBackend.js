const express = require('express');
const confirmationBackend = express.Router();
const UserModel = require('../models/UserModel');
const nodeMailer = require('nodemailer');

confirmationBackend.route('/confirmAccount').post((req,res)=>{

    let confirmationCode = Math.floor((Math.random()*100000)+10000).toString();

    UserModel.findOneAndUpdate({email:req.body.email},{$set:{confirmationCode:confirmationCode}},(err,doc)=>{
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
        to: req.body.email,
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
});
confirmationBackend.route('/confirm').post((req,res)=>{
    UserModel.findOne({userName:req.body.userName},(err,user)=>{
       if(err)
       {
           res.send("server error");
       }
       else
       {

           if(user.confirmationCode ===(req.body.confirmationCode))
           {
               user.isLoggedIn=true;
               user.save((err)=>{
                  if(err)
                  {
                      res.send("server error");
                  }
               });
               res.send("success");
           }
           else {

               res.send("incorrect confirmation code");
           }
       }
    });
});
module.exports = confirmationBackend;