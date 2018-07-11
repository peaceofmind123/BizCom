const express = require('express');
const uaRouter = express.Router();

//define uaRouter get, post, put methods here
uaRouter.route('/signup').post((req,res)=>{
    // the req.body object now contains the object sent from the client
    console.log(req.body);
    res.send("Success")


});
module.exports = uaRouter;
