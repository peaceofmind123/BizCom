const express = require('express');
const uaRouter = express.Router();

//define uaRouter get, post, put methods here
uaRouter.route('/signup').post((req,res)=>{
    print(req.body)



});
module.exports = uaRouter;
