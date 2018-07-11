const express = require('express');
const signupForm = express.Router();


signupForm.route('/').post((req,res)=>{
    // the req.body object now contains the object sent from the client
    console.log(req.body);
    res.send("Success");


});

module.exports=signupForm;