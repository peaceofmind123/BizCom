const express = require('express');
const profileRouter = express.Router();
const fs = require('fs');
const multer = require('multer');
const upload = multer({dest:'../static/photos/'});
//define profileRouter get, post, put methods here
profileRouter.post('/uploadImageTest',upload.single('image'),(req,res)=> {
    console.log(req.file);
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

module.exports = profileRouter;
