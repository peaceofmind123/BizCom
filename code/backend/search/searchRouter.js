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
        const regex = new RegExp(escapeRegex(req.query.query), 'gi');
        UserModel.find(
            {
                $or:[{userName:{$regex:regex,$options:'i'}},{fName:{$regex:regex,$options:'i'}},
                    {lName:{$regex:regex,$options:'i'}},{city:{$regex:regex,$options:'i'}},
                    {country:{$regex:regex,$options:'i'}}]

            }
        )

            .exec((err,results)=>{
            console.log(results);
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
                        let companyNames = [];
                        console.log(results);
                        results.forEach(result=>{
                            if(result.userType==="business")
                                companyNames.push(result.userName);
                        });


                        console.log(companyNames);
                        res.json(companyNames);
                    }

                }
            });
    }
});
function escapeRegex(text) {
    return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
};
module.exports = searchRouter;
