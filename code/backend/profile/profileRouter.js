const express = require('express');
const profileRouter = express.Router();
const fs = require('fs');
const multer = require('multer');
const upload = multer({dest:'./static/photos/'});
const path = require('path');
//define profileRouter get, post, put methods here
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
