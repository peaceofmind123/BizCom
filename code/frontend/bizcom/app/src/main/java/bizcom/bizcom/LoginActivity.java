package bizcom.bizcom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public final String EXTRA_USER = "bizcom.bizcom.USER";
    private EditText Username;
    private EditText Password;
    private Button Login;
    private TextView Signup; //this is for unregistered users
    private TextView ForgotPassword;
    String jsonUrl = "http://192.168.1.67:8000/userAccounts/login";



   /* protected void ToastForEmptyField(boolean val){
        if(val){
            Toast.makeText(this,"empty username", Toast.LENGTH_SHORT).show();
            Username.setError("required field");
        }
        else{
            Toast.makeText(this,"empty password", Toast.LENGTH_SHORT).show();
            Username.setError("required field");
        }
    }
    */
    //RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);


    public boolean isNotNull(String user, String pass) {
        if ((user.length() != 0) && (pass.length() != 0)) {
            //if not null and credentials match
            /*if((user.equals("aashish")) && (pass.equals("sayami"))){
                Intent intent = new Intent(this,HomeActivity.class);
                startActivity(intent);
            */
            return true;
        } else {
            return false;
        }
    }
           /*//if credentials  dont match
            else{
                //we need an alert dialog box here
                AlertDialog.Builder inCorrectAlert = new AlertDialog.Builder(this);
                inCorrectAlert.setMessage("Incorrect password or username");
                inCorrectAlert.setTitle("Error");
                inCorrectAlert.setCancelable(true);
                inCorrectAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //now create the dialog box
                AlertDialog alert = inCorrectAlert.create();
                //display the AlerDialog
                alert.show();
            }
        }
        //if null
        else{
            //we need an empty field error msg here
            if(TextUtils.isEmpty(user)){  //using null or .equals (null) wasnt compatible with use of space. however this still
                //doesnt help
                //we make a toast here


                 Toast.makeText(getApplicationContext(),"empty username", Toast.LENGTH_SHORT).show();
                 Username.setError("required field");


            }
            if (TextUtils.isEmpty(pass)){
                //next toast here too



                 Toast.makeText(getApplicationContext(),"empty password", Toast.LENGTH_SHORT).show();
                 Password.setError("required field");


            }
        }

    }*/


    public void callSignupAcitvity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Username = (EditText) findViewById(R.id.etUsername);
        Password = (EditText) findViewById((R.id.etPass));
        Login = (Button) findViewById((R.id.btnlogin));
        Signup = (TextView) findViewById((R.id.etSignup));
        ForgotPassword = findViewById(R.id.textForgotPassword);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callSignupAcitvity(); // this call is compulsory call idk why
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean NotNull = isNotNull(Username.getText().toString(), Password.getText().toString());

                String name, pass = null;
                name = Username.getText().toString();
                try {
                    pass =AESCrypt.encrypt(Password.getText().toString());
                    pass = pass.substring(0,pass.length()-1);
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogFragmentHelper.showDialogFragment(LoginActivity.this,"Something went wrong, please try again");
                    recreate();
                }


                if (NotNull) {

                   /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST)

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userName", name);
                            params.put("password", pass);
                            return super.getParams();
                        }

                    };*/

                   //creating a json object to pass credentials to server
                   JSONObject passCredentials = new JSONObject();
                    try {
                        passCredentials.put("userName", name);
                        passCredentials.put("password", pass);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //String Credentials = passCredentials.toString();


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, jsonUrl, passCredentials, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                           try {
                                //String uName= response.getString("userName");


                                //if credentials dont match
                                response.getBoolean("result");

                                    AlertDialog.Builder inCorrectAlert = new AlertDialog.Builder(LoginActivity.this);
                                    inCorrectAlert.setMessage("Incorrect username or password");
                                    inCorrectAlert.setTitle("Error");
                                    inCorrectAlert.setCancelable(true);
                                    inCorrectAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    //now create the dialog box
                                    AlertDialog alert = inCorrectAlert.create();
                                    //display the AlerDialog
                                    alert.show();



                                //if credentials match







                            } catch (JSONException e) {
                               try
                               {
                                   String userType = response.getString("userType"); //test to see if correct json is sent
                                   String user;
                                   user = response.toString();
                                   if(userType.equals("business"))
                                   {
                                       Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                                       intent.putExtra(SignupActivity.EXTRA_USER_JSON,user);
                                       startActivity(intent);
                                   }
                                   else
                                   {
                                       Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                       intent.putExtra(SignupActivity.EXTRA_USER_JSON,user);
                                       startActivity(intent);
                                   }

                               }
                               catch(JSONException e1)
                               {
                                    DialogFragmentHelper.showDialogFragment(LoginActivity.this,R.string.err_general_dialogMsg);
                               }




                        }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            DialogFragmentHelper.showDialogFragment(LoginActivity.this,getString(R.string.err_general_dialogMsg));
                            error.printStackTrace();


                        }
                    });


                    MySingleton.getMinstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);

                 //if null
                } else {

                    if (TextUtils.isEmpty(name)) {  //using null or .equals (null) wasnt compatible with use of space. however this still
                        //doesnt help
                        //we make a toast here


                        Toast.makeText(getApplicationContext(), "empty username", Toast.LENGTH_SHORT).show();
                        Username.setError("required field");


                    }
                    if (TextUtils.isEmpty(pass)) {
                        //next toast here too


                        Toast.makeText(getApplicationContext(), "empty password", Toast.LENGTH_SHORT).show();
                        Password.setError("required field");

                    }
                }
            }
        });
    }
}

