const express = require('express');
const uaRouter = express.Router();


//define the backend objects defined in the collaboration diagram here
const signupForm = require('./signupForm.js');

//use them here
uaRouter.use('/signup',signupForm);



module.exports = uaRouter;
