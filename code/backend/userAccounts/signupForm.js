const express = require('express');
const signupForm = express.Router();
const UserModel = require('../models/UserModel');




signupForm.route('/').post((req,res)=>{
    // the req.body object now contains the object sent from the client

    UserModel.findOne({userName:req.body.userName},(err,user)=>{
        if(err)
        {
            console.log(err);
            return;
        }

        if(user!==null)
        {
            console.log(user);
           let message = "User already registered!";
            console.log(message);
            res.send(message);
        }
        else
        {
           let message = "success";
           let user = new UserModel(req.body);
            user.save((err)=>{
                    if(err)
                    {
                        console.log(err);
                        message = err;
                    }
                    else
                    {
                        console.log("User saved!")
                    }
                });

            res.send(message);



        }
    });




});

module.exports=signupForm;