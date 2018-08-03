const express = require('express');
const searchRouter = express.Router();
const UserModel = require('../models/UserModel');
//define searchRouter get, post, put methods here
searchRouter.get('/',(req,res)=>{
    if(req.query.query ===null)
    {
        console.log("invalid query");
        res.json({'response':'error'});
    }
    else
    {
        UserModel.find(
            {
                $text:{$search:req.query.query},

            }
        )

            .exec((err,results)=>{
                if(err)
                {
                    res.json({'response':'server error'});

                }
                else
                {
                    if(results===null)
                    {
                        console.log('nothing found!!');
                        res.json({'response':'error'})
                    }
                    else
                    {
                        console.log(results);
                        let companyNames = [];
                        companyNames.push(results.userName);
                        console.log(companyNames);
                        res.json(companyNames);
                    }

                }
            });
    }
});
module.exports = searchRouter;
