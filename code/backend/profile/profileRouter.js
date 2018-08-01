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
        res.json({'response':"error"});
    }
    fs.readFile(req.file.path, function (err, data) {

        fs.writeFile(newPath, data, function (err) {
            if (err) {
                res.json({'response': "server error"});
            }
            else
            {

                UserModel.findOneAndUpdate({userName:userName},{$set:{profilePicPath:newPath}},(err,user)=>{
                    if(err)
                    {
                        res.json({'response':'error'});
                    }
                    if(user===null)
                    {
                        res.json({'response':'username not found'});
                    }
                    else res.json({'response': "success"});
                });

            }
        });
    });

});
profileRouter.post('/uploadAdPic',upload.single('image'),(req,res)=> {
    let userName = req.header('userName');
    let dirname = req.file.destination;
    let newPath = dirname + userName + "mainAdPic.jpg";
    if (userName === null) {
        res.json({'response': "error"});
    }
    fs.readFile(req.file.path, function (err, data) {

        fs.writeFile(newPath, data, function (err) {
            if (err) {
                res.json({'response': "server error"});
            }
            else {
                UserModel.findOneAndUpdate({userName: userName}, {$set: {mainAdPicPath: newPath}}, (err, user) => {
                    if (err) {
                        res.json({'response': 'error'});
                    }
                    if (user === null) {
                        res.json({'response': 'username not found'});
                    }
                    else res.json({'response': "success"});
                });
            }
        });
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
module.exports = profileRouter;
