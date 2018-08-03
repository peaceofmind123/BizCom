const express = require('express');
const profileRouter = express.Router();
const fs = require('fs');
const multer = require('multer');
const upload = multer({dest:'./static/photos/'});
const path = require('path');
const UserModel = require('../models/UserModel');
//define profileRouter get, post, put methods here
profileRouter.post('/uploadProfilePic',upload.single('image'),(req,res)=>{
    let userName = req.header('userName');
    let dirname =req.file.destination;
    let newPath = dirname + userName+"profilePic.jpg";
    if(userName ===null)
    {
        console.log('error');
        res.json({'response':"error"});
    }
    UserModel.findOne({userName:userName},(err,user)=>{
       if(err)
       {
           console.log('database error');
           res.json({'response':'error'});
       }
       if(user===null)
        {
            console.log('username not found');
            res.json({'response':'username not found'});
        }
        else
       {
           if(user.isLoggedIn)
           {
               fs.readFile(req.file.path, function (err, data) {

                   fs.writeFile(newPath, data, function (err) {
                       if (err) {
                           console.log('can\'t write to fs');
                           res.json({'response': "server error"});
                       }
                       else
                       {
                           user.save((err)=>{
                               if(err)
                               {
                                   console.log('couldn\'t save user');
                                   res.json({'response':'database error'});
                               }

                               else
                               {
                                   console.log('success');
                                   res.json({'response':'success'});
                               }

                           });
                       }
                   });
               });
           }
           else {
               console.log('not logged in');
               res.json({'response':'not logged in'});
           }
       }
    });


});

profileRouter.post('/uploadAdPic',upload.single('image'),(req,res)=>{
    let userName = req.header('userName');
    let dirname =req.file.destination;
    let newPath = dirname + userName+"mainAdPic.jpg";
    if(userName ===null)
    {
        res.json({'response':"error"});
    }
    UserModel.findOne({userName:userName},(err,user)=>{
        if(err)
        {
            res.json({'response':'error'});
        }
        if(user===null)
        {
            res.json({'response':'username not found'});
        }
        else
        {
            if(user.isLoggedIn)
            {
                user.mainAdPicPath = newPath;

                fs.readFile(req.file.path, function (err, data) {

                    fs.writeFile(newPath, data, function (err) {
                        if (err) {
                            res.json({'response': "server error"});
                        }
                        else
                        {
                            user.save((err)=>{
                                if(err)
                                    res.json({'response':'database error'});
                                else
                                    res.json({'response':'success'});
                            });


                        }
                    });
                });
            }
            else {
                res.json({'response':'not logged in'});
            }
        }
    });


});
profileRouter.post('/uploadImageTest',upload.single('image'),(req,res)=> {
    console.log(req.header('userName'));
    fs.readFile(req.file.path, function (err, data) {
        let dirname =req.file.destination;
        let newPath = dirname + req.file.originalname;
        fs.writeFile(newPath, data, function (err) {
            if (err) {
                res.json({'response': "Error"});
            } else {
                res.json({'response': "Saved"});
            }
        });
    });
});
profileRouter.get('/getProfilePic',(req,res)=>{
   let userName = req.query.userName;
   let file = userName+"profilePic.jpg";
   let filePath = path.join(__basedir,'/static/photos/',file);
   console.log(filePath);
    fs.readFile(filePath,(err,image)=>{
        if(err)
        {
            console.log(err);
            res.json({'response':'error'});
        }
        else
        {
            res.writeHead(200,{'Content-Type':'image/jpg'});
            res.end(image,'binary');
        }

    });
});
profileRouter.get('/getMainAdPic',(req,res)=>{
    let userName = req.query.userName;
    let file = userName+"mainAdPic.jpg";
    let filePath = path.join(__basedir,'/static/photos/',file);
    fs.readFile(filePath,(err,image)=>{
        if(err)
        {
            console.log(err);
            res.json({'response':'error'});
        }
        else
        {
            res.writeHead(200,{'Content-Type':'image/jpg'});
            res.end(image,'binary');
        }

    });
});
profileRouter.get('/viewImageTest',(req,res)=>{

    let file = req.query.image;
    let filePath = path.join(__basedir,'/static/photos',file);
    fs.readFile(filePath,(err,image)=>{
        if(err)
        {
            console.log(err);
            res.json({'response':'error'});
        }
        else
        {
            res.writeHead(200,{'Content-Type':'image/jpg'});
            res.end(image,'binary');
        }

    });
});
profileRouter.post('/updateAdditionalInfo',(req,res)=>{
    console.log(req.body);
    if(!req.body.userName || !req.body.additionalInfo)
    {
        console.log('wrong body params');
        res.json({'response':'error'});
    }
    else
    {
        UserModel.findOne({userName:req.body.userName},(err,user)=>{
           if(err)
           {
               console.log("username not found");
               res.json({'response':'error'});
           }
           else {
               if(!user.isLoggedIn)
               {
                   console.log("not logged in");
                   res.json({'response':'not logged in'});
               }
               else
               {
                   user.additionalInfo = req.body.additionalInfo;
                   user.save(err=>{
                      if(err)
                      {
                          console.log("database error");
                          res.json({'response':'error'});
                      }
                      else {
                          console.log("success");
                          res.json({'response':'success'});
                      }
                   });
               }
           }
        });
    }
});
module.exports = profileRouter;
