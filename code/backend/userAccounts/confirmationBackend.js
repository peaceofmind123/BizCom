const express = require('express');
const confirmationBackend = express.Router();
const UserModel = require('../models/UserModel');
const nodeMailer = require('nodemailer');

//a purely fabricated controller for the confirmAccount use Case

confirmationBackend.route('/confirmAccount').post((req,res)=>{

  UserModel.confirmAccount(req.body.email,res)
});

confirmationBackend.route('/confirm').post((req,res)=>{
    UserModel.findOne({userName:req.body.userName},(err,user)=>{
       if(err)
       {
           res.send("server error");
       }
       else
       {

           if(user.confirmationCode ===(req.body.confirmationCode) && user.isConfirmed)
           {

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