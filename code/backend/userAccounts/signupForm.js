const express = require('express');
const signupForm = express.Router();
const UserModel = require('../models/UserModel');
const async = require('async');



signupForm.route('/').post((req,res)=>{
    // the req.body object now contains the object sent from the client
    let duplicates = [];
    let executecounter = 0; //kinda like a semaphore
    UserModel.findOne({userName:req.body.userName},(err,user) =>{

        if(err)
            console.log(err);
        else if(user!==null)
        {
            duplicates.push("username");
        }
        executecounter++;
    });
    UserModel.findOne({email:req.body.email},(err,user)=>{
        if(err)
            console.log(err);
        else if(user!==null) //if user is already there
        {
            duplicates.push("email");
        }
        executecounter++;
    });
    UserModel.findOne({phone:req.body.phone},(err,user)=>{

        if(err)
            console.log(err);
        else if(user!==null) //if user is already there
        {
            duplicates.push("phone");
        }
        executecounter++;
    });






});

module.exports=signupForm;