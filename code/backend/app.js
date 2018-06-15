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

const port = process.env.port || 8000;

//application initiation
const app = express();

//application middleware use declarations
app.use(bodyParser.urlencoded({extended:true}));
app.use(bodyParser.json());

//database connection
//replace <dbuser> by your database username and <dbpassword> by your database password

//const db = mongoose.connect('mongodb://<dbuser>:<dbpassword>@ds141401.mlab.com:41401/bizcom');

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
    res.send("API starting point!!");
});

app.listen(port,()=>{
    debug(" listening on port "+ chalk.green(port));
})