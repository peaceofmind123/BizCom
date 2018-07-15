const express = require('express');
const signupForm = express.Router();
const UserModel = require('../models/UserModel');
const as = require('async');

signupForm.route('/').post((req,res)=> {
    let responseString = "";
    UserModel.count({userName:req.body.userName},(err,count)=>{
       if(err)
           res.status(500).send('database error');
       else
       {
           if(count>0)
                responseString+=" username ";
           UserModel.count({email:req.body.email},(err,count)=>{
               if(err) {
                   res.send('database error');
               }
               else
               {
                   if(count>0)
                        responseString+= " email ";
                   UserModel.count({phone:req.body.phone},(err,count)=>{
                       if(err)
                       {
                           res.send("database error");

                       }
                       else
                       {
                           if(count > 0)
                               responseString+= " phone ";
                           if(responseString==="") //no duplicates
                           {
                               let user = new UserModel(req.body);
                               user.save((err)=>{
                                  if(err)
                                  {
                                      res.send("database error");
                                  }
                                  res.send("success");
                               });

                           }
                           else
                               res.send(responseString);
                       }
                   });
               }
           });
       }

    });
});
module.exports=signupForm;