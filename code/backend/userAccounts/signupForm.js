const express = require('express');
const signupForm = express.Router();
const UserModel = require('../models/UserModel');
const as = require('async');

signupForm.route('/').post((req,res)=> {
    let responseString = "";
    UserModel.find({isConfirmed:false},(err,doc)=>
    {
       if(err)
           console.log(err);
       else
           console.log(doc);
    });
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
                               let user = new UserModel();
                               user.isConfirmed = false;
                               user.fName = req.body.fName;
                               user.lName = req.body.lName;
                               user.userName = req.body.userName;
                               user.email = req.body.email;
                               user.password = req.body.password;
                               user.phone = req.body.phone;
                               user.city = req.body.city;
                               user.country = req.body.country;
                               user.userType = req.body.userType;


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