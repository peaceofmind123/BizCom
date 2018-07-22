const express = require('express');
const app = express();
const bodyParser = require('body-parser');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:false}));


//const urlencodedParser = bodyParser.urlencoded({ extended: false });


const mongoose = require('mongoose');

const userModel = require('./models/userSchema');

const mongoDB = 'mongodb://127.0.0.1/finalusers';
mongoose.connect(mongoDB);

mongoose.Promise = global.Promise;


//just a test to register some of the user
/*app.post('/test/api/store', (req,res)=>{


  //req.body=JSON.stringify(req.body);
  let user = new userModel({
     fName:req.body.fName,
     lName:req.body.lName,
     userName: req.body.userName,
     password:req.body.password });
     user.save()
     .then(item => {
      res.send("item saved to database");
      console.log(req.body);
    })
    .catch(err => {
      res.status(400).send("unable to save to database");
    });



});*/



app.post('/test/api/search',(req,res)=>{

  userModel.findOne({ userName: req.body.userName }, function (err, model) {
    if (err) return handleError(err);
    if(model){
      //res.json(model);
      if ((req.body.password)===(model.password)){
        res.json(model);
        console.log(model);
      }
      else{
        res.send(JSON.stringify({
          error:'username or password incorrect'

        }))
        console.log('im inside the 3ndif');
      }

    }
    else{
      res.send(JSON.stringify({
        error:'username or password incorrect'
      }))
     console.log('im inside the 2nd if ');
    }
  });

});

app.listen(3000, ()=>{
  console.log('listening on port 3000');
});
