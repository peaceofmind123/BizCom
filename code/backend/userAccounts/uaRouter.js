const express = require('express');
const uaRouter = express.Router();


//define the backend objects defined in the collaboration diagram here
const signupForm = require('./signupForm.js');
const loginForm = require('./loginForm.js');
const confirmationBackend = require('./confirmationBackend');
//use them here
uaRouter.use('/signup',signupForm);
uaRouter.use('/login',loginForm);
uaRouter.use('/confirmation',confirmationBackend);

module.exports = uaRouter;
