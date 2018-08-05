const express = require('express');
const uaRouter = express.Router();
const forgotPasswordBackend = require('./forgotPasswordBackend');
const UserModel = require('../models/UserModel');
//define the backend objects defined in the collaboration diagram here
const signupForm = require('./signupForm.js');
const loginForm = require('./loginForm.js');
const confirmationBackend = require('./confirmationBackend');
//use them here
uaRouter.use('/signup',signupForm);
uaRouter.use('/login',loginForm);
uaRouter.use('/confirmation',confirmationBackend);
uaRouter.use('/forgotPassword',forgotPasswordBackend);

uaRouter.post('/getUserDetails',(req,res)=>{
    let errorResponse = {'response':'error'};
   if(req.body.userName)
   {
       UserModel.findOne({userName:req.body.userName},(err,user)=>{

           if(err)
          {
              res.json(errorResponse);
          }
          else if(user===null)
           {
               res.json(errorResponse);
           }
           else {
               res.json(user);
           }
       });
   }
});
uaRouter.post('/logout',(req,res)=>{
    if(req.body.userName)
    {
        UserModel.findOne({userName:req.body.userName},(err,user)=>{
           if(user && user.isLoggedIn)
           {
               user.isLoggedIn = false;
               user.loginToken = "";
               user.save(err=>{
                   if(err)
                   {
                       res.json({'response':'error'});
                   }
                   else {
                       res.json({'response':'success'});
                   }
               });
           }
           else {
               res.json({response:'error'});
           }
        });
    }
});
module.exports = uaRouter;
