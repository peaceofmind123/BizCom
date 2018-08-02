const express = require('express');
const homeRouter = express.Router();
const fs = require('fs');
const multer = require('multer');
const upload = multer({dest:'./static/photos/'});
const path = require('path');
const UserModel = require('../models/UserModel');

homeRouter.get('/getCompanyNames',(req,res)=>{
   let userName = req.query.userName;
   if(userName===null)
   {
       console.log("invalid query");
       res.json({'response':'error'});
   }
   UserModel.find({userType:'business'})
       .limit(6)
       .exec((err,users)=>{
            if(err)
            {
                console.log('couldn\'t find business users');
                res.json({'response':'error'});
            }
            else if(users!==null)
            {

                let companyNames = {
                    company1: users[0]?users[0].userName:null,
                    company2: users[1]?users[1].userName:null,
                    company3: users[2]?users[2].userName:null,
                    recCompany1:users[3]?users[3].userName:null,
                    recCompany2:users[4]?users[4].userName:null,
                    recCompany3:users[5]?users[5].userName:null
                };
                res.json(companyNames);
            }
            else res.json({'response':'error'});

       });

});
module.exports=homeRouter;