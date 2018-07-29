const express = require('express');
const profileRouter = express.Router();
const fs = require('fs');
//define profileRouter get, post, put methods here
profileRouter.route('/uploadImageTest').post((req,res)=> {
    console.log(req);


});

module.exports = profileRouter;
