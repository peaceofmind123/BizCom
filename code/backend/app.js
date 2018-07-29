//the project uses javascript es6 as its primary language

// requires and constants
//common useful requires
const express = require('express');
const mongoose = require('mongoose');
const debug = require('debug')('app');
const bodyParser = require('body-parser');
const chalk = require('chalk');
const morgan = require('morgan');
const path = require('path');
const cookieParser = require('cookie-parser');
const flash = require('connect-flash');
const session = require('express-session');
const passport = require('passport');
const multer = require('multer');
//the local strategy is for our own authentication
const LocalStrategy = require('passport-local').Strategy;
//this is in accordance with the express-validator 5.* update.. no need of middleware declarations
const {check, validationResult} =require('express-validator/check');

const port = process.env.port || 8000;

//application initiation
const app = express();

//application middleware use declarations
app.use(bodyParser.json());
app.use(cookieParser());
//the session object to be used throughout the app
app.use(session({
    secret: 'teamBaagh',
    saveUninitialized: true,
    resave: true
}));


app.use(passport.initialize());
app.use(passport.session());
//used for returning validation error messages
app.use(flash());
app.use(morgan('tiny'));

//database connection

mongoose.connect('mongodb://nripendra:havoc123@ds141401.mlab.com:41401/bizcom');
mongoose.Promise = global.Promise;
const db = mongoose.connection;
db.on('error', (err)=>{
    debug("couldn't connect to database!!");

});

//architectural division

//the user accounts subsystem
const uaRouter =require('./userAccounts/uaRouter');

//the profile subsystem
const profileRouter = require('./profile/profileRouter');

//the recommendation subsystem
const recRouter = require('./recommendation/recRouter');

//the search subsystem
const searchRouter = require('./search/searchRouter');

//subsystems use declarations
app.use('/userAccounts',uaRouter);
app.use('/profile',profileRouter);
app.use('/search', searchRouter);
app.use('/recommendation',recRouter);
//the api starting point
app.get('/',(req,res)=>{
    debug('get request recieved');
    res.send("API starting point!!");
});

app.listen(port,()=>{
    debug(" listening on port "+ chalk.green(port));
});