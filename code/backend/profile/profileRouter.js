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
        else if(user===null)
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
profileRouter.get('/getExtraAdPicNo',(req,res)=>{
   let errorJson = {'response':'error'};
    if(!req.query.userName)
   {
        res.json(errorJson);
   }
   else {
        UserModel.findOne({userName:req.query.userName},(err,user)=>{
            if(user.additionalAdPicsPath)
            {
                res.json({'number':user.additionalAdPicsPath.length});
            }
            else
            {
                res.json({'number':0});
            }
        });
    }
});
profileRouter.get('/getExtraAdPic',(req,res)=>{
   if(!req.query.userName||!req.query.number || isNaN(req.query.number))
   {
       res.json({'response':'error'});
   }
   else {
       UserModel.findOne({userName:req.query.userName},(err,user)=>{
          if(!user.additionalAdPicsPath)
          {
              res.json({'response':'error'});
          }
          else {
              if(req.query.number>=user.additionalAdPicsPath.length)
              {
                  res.json({'response':'error'});
              }
              else {
                  let filePath = path.join(__basedir,user.additionalAdPicsPath[req.query.number]);
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
              }

          }
       });
   }
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
profileRouter.post('/deleteExtraAdPic',(req,res)=>{
   if(!req.body.userName || !req.body.number)
   {
       res.json({response:"error"});
   }
   else {
       UserModel.findOne({userName:req.body.userName},(err,user)=>{
           if(err || !user.isLoggedIn)
           {
               res.json({response:'error'});
           }
           else
           {
               delete user.additionalAdPicsPath[req.body.number];
               console.log('success');
               user.save();
               res.json({response:'success'});

           }
       })
   }
});
profileRouter.post('/updateAdditionalInfo',(req,res)=>{
    console.log(req.body);
    if(!req.body.userName || !req.body.additionalInfo || !req.body.loginToken)
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
               if(!user.isLoggedIn || user.loginToken!==req.body.loginToken || req.body.loginToken==="")
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
profileRouter.post('/uploadExtraAdPic',upload.single('image'),(req,res)=> {
    let errorResponse = {'response': 'error'};
    if (!req.header('userName') || !req.file) {
        res.json(errorResponse);
    }
    else {
        UserModel.findOne({userName: req.header('userName')}, (err, user) => {
            let errorResponse = {response: 'error'};
            if (err) {
                res.json(errorResponse);
            }
            else {
                if (user === null) {
                    res.json(errorResponse);
                }
                else if (!user.isLoggedIn) {
                    res.json(errorResponse);
                }
                else {

                    let num = req.header('number');
                    if(num && num<user.additionalAdPicsPath.length)
                    {
                        console.log('yes');

                        user.additionalAdPicsPath.splice(num,0,req.file.path);
                        user.save();
                    }
                    else {
                        user.additionalAdPicsPath.push(req.file.path);
                        console.log('no');
                        user.save();
                    }

                    res.json({response:'trial'});

                }
            }
        });
    }
});

profileRouter.post('/postRating',(req,res)=>{
   if(!req.body.userName || !req.body.rating ||isNaN(req.body.rating) || !req.body.viewerUserName)
   {
       res.json({"response": "invalid request"});
   }
   else
   {
       UserModel.findOne({userName:req.body.viewerUserName},(err,viewer)=>{

          if(!viewer)
          {
              console.log("user not found");
              res.json({"response":"user not found"});
          }
          if(!viewer.isLoggedIn)
          {
              console.log("you are not logged in");
              res.json({"response":"not logged in"});
          }
          else {
                UserModel.findOne({userName:req.body.userName},(err,user)=>{

                    if(user.userType!=="business")
                    {
                        console.log("Wrong user type");
                        res.json({"response":"error"});
                    }
                    else {
                        let i=0;
                        let found = false;
                        for(;i<user.userReviews.length;i++)
                        {
                            if(user.userReviews[i].userName ===viewer.userName)
                            {
                                user.userReviews[i].rating = req.body.rating;
                                found=true;
                                break;
                            }
                        }
                        if(found)
                        {
                            user.save(err=>{
                                if(err)
                                {
                                    console.log("database error");
                                    res.json({"response":"error"});
                                }
                                else {
                                    console.log("success");
                                    res.json({"response":"success"});
                                }
                            });

                        }
                        else {
                            user.userReviews.push({userName:viewer.userName,
                                rating:req.body.rating});
                            user.save(err=>{
                                if(err)
                                {
                                    console.log("database error");
                                    res.json({"response":"error"});
                                }
                                else {
                                    console.log("success");
                                    res.json({"response":"success"});
                                }
                            });
                        }

                    }
                });
          }
       });
   }
});
profileRouter.get('/getRatings',(req,res)=>{
   if(!req.query.userName)
   {
       console.log("Invalid request");
       res.json({'response':'invalid query parameter'});
   }
   else
   {
       UserModel.findOne({userName:req.query.userName},(err,user)=>{
          console.log(user);
           if(user.userReviews)
          {
              console.log(user.userReviews);
              let sum=0;
              for(let i =0;i<user.userReviews.length;i++)
              {
                  sum+=user.userReviews[i].rating;
              }
              if(user.userReviews.length>0)
                res.json({'rating':sum/user.userReviews.length});
              else {
                  res.json({'rating':0});
              }
          }
          else {
              console.log("no rating found");
              res.json({rating:0});
          }
       });
   }
});
module.exports = profileRouter;
