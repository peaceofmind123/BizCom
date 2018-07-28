const express = require('express');
const loginForm = express.Router();
const UserModel = require('../models/UserModel');

loginForm.route('/').post((req,res)=>{
    UserModel.findOne({ userName: req.body.userName }, function (err, model) {
        if (err) return handleError(err);

        if(model){

            //if the username is registered compare the passwords

            //if password matches
            if ((req.body.password)===(model.password)){
                res.json(model);
                console.log(model);
            }

            //if password doesnt match
            else{
                //res.send(400,{"result":false});
                //res.status(400).send({"result":false});
                res.send({"result":true}); //to make the if condition in android studio work

            }

        }
        //if the username is not registered
        else{

            //res.send(400,{"result":false});
            //res.status(400).send({"result":false});
            res.json({"result":true});

        }
    });

});

module.exports = loginForm;