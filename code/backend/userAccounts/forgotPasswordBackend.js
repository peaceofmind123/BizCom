const express = require('express');
const forgotPasswordBackend = express.Router();
const UserModel = require('../models/UserModel');
const nodeMailer = require('nodemailer');

forgotPasswordBackend.route('/sendForgotCode').post((req,res)=>{
    let forgotPasswordCode = Math.floor((Math.random()*100000)+10000).toString(); //a 5-6 digit random code

    UserModel.findOneAndUpdate({email:req.body.email},{$set:{forgotPasswordCode:forgotPasswordCode}},(err,doc)=>{
        if(err)
            res.send("error");
        else if(doc===null)
        {
            res.send("email not found");
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


forgotPasswordBackend.route('/handleForgotCode').post((req,res)=>{
   if(req.body.forgotPasswordCode === null || req.body.email === null)
       res.send("error");
   else
   {
       try {
           UserModel.findOne({email:req.body.email},(err,user)=>{
              if(err)
                  res.send("email not found");
              else
              {
                  if(user.forgotPasswordCode ===req.body.forgotPasswordCode)
                  {
                      user.passwordChangeRequest = true;
                      user.save((err)=>{
                          if(err)
                          {
                              res.send("database error"); //need to try again if this happens
                          }
                      });
                      res.send("success");
                  }
                  else
                  {
                      res.send("invalid passCode");
                  }
              }
           });
       }
       catch(err)
           {
                res.send("server error");
           }

   }
});

forgotPasswordBackend.route('/updatePassword').post((req,res)=>{
   if(req.body.password === null || req.body.email === null)
       res.send("error");
   else {
       try {
           UserModel.findOne({email:req.body.email},(err,user)=>{
              if(err)
                  res.send("server error");
              if(user === null)
                  res.send("email not found");
              else {
                  if(user.passwordChangeRequest) //if the request was sent by the user
                  {
                      user.password = req.body.password;
                      user.passwordChangeRequest = false;
                      user.save((err)=>{
                         if(err)
                             res.send("database error");
                      });
                      res.send("success");
                  }
                  else {
                      res.send("invalid request");
                  }
              }
           });
       }
       catch(err)
       {
           res.send("server error");
       }
   }
});
module.exports = forgotPasswordBackend;