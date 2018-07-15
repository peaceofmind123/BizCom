package bizcom.bizcom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;



import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;


import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Keys to pass params as intent extras
    // this is the key to the json object being passed
    public static final String EXTRA_USER = "com.bizcom.bizcom.USER";


    String fName;
    EditText fNameText;
    EditText lNameText;
    EditText userNameText;
    EditText emailText;
    EditText passwordText;
    EditText confirmPasswordText;
    EditText phoneText;
    EditText cityText;
    Spinner countrySpinner;
    RadioGroup userSelectGroup;
    CheckBox tosAccept;
    Button signupBtn;
    String lName;
    String userName;
    String email;
    String password;
    String confirmPassword;
    String phone;
    String city;
    String country;
    AwesomeValidation awesomeValidation;
    ArrayList<String> countries;


    public void signUp(View view) throws Exception {
        String json= getJson();
        String url = "http://192.168.1.67:8000/userAccounts/signup";
        new SignupPostTask(this).execute(url,json);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getCountries();
        updateUI();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(SignupActivity.this, R.id.fName, "[a-zA-Z\\s]+", R.string.err_fname);
        awesomeValidation.addValidation(SignupActivity.this, R.id.lName, "[a-zA-Z\\s]+", R.string.err_lname);
        awesomeValidation.addValidation(SignupActivity.this, R.id.userName, "[a-zA-Z\\s]+", R.string.err_username);
        awesomeValidation.addValidation(SignupActivity.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(SignupActivity.this, R.id.password, regexPassword, R.string.err_password);
        awesomeValidation.addValidation(SignupActivity.this, R.id.confirmPassword, R.id.password, R.string.err_confirmpassword);
        awesomeValidation.addValidation(SignupActivity.this, R.id.phone, RegexTemplate.TELEPHONE, R.string.err_phone);
        awesomeValidation.addValidation(SignupActivity.this, R.id.city, "[a-zA-Z\\s]+", R.string.err_city);

        awesomeValidation.addValidation(this, R.id.country, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("-select country-")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.err_country);

        signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {
                    try {
                        signUp(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        tosAccept.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //enable signup button if tos accept is selected
                    signupBtn.setEnabled(true);
                    signupBtn.setBackgroundColor(SignupActivity.this.getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    signupBtn.setEnabled(false);
                    signupBtn.setBackgroundColor(SignupActivity.this.getResources().getColor(R.color.graycolor));


                }
            }
        });
    }

    private String getJson() throws Exception {
        fName = fNameText.getText().toString();
        lName = lNameText.getText().toString();
        userName = userNameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();
        phone = phoneText.getText().toString();
        city = cityText.getText().toString();
        country = countrySpinner.getSelectedItem().toString();
        RadioButton rb = findViewById(userSelectGroup.getCheckedRadioButtonId());
        String userType = rb.getText().toString();

        //encryption
        String encryptedPass = AESCrypt.encrypt(password);
        String encryptedPassConfirm = AESCrypt.encrypt(confirmPassword);
        String encryptedPhone = AESCrypt.encrypt(phone);
        encryptedPass=encryptedPass.substring(0,encryptedPass.length()-1); //apparently there is a /n at the end that causes errors, so truncated it
        encryptedPassConfirm = encryptedPassConfirm.substring(0,encryptedPassConfirm.length()-1); // gotta consider it on the backend too
        encryptedPhone = encryptedPhone.substring(0,encryptedPhone.length()-1);

        // converting to json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fName",fName);
        jsonObject.put("lName",lName);
        jsonObject.put("userName",userName);
        jsonObject.put("email",email);
        jsonObject.put("password",encryptedPass);
        jsonObject.put("confirmPassword",encryptedPassConfirm);
        jsonObject.put("phone",encryptedPhone);
        jsonObject.put("city",city);
        jsonObject.put("country",country);
        if(userType.equals("Sign Up as Business"))
            userType = "business";
        else
            userType = "general";
        jsonObject.put("userType",userType);
        return jsonObject.toString();
    }

    private void getCountries() {
        countries = new ArrayList<String>();
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        countries.add(0,"-select country-");
    }

    private void updateUI() {
        fNameText = findViewById(R.id.fName);
        lNameText = findViewById(R.id.lName);
        userNameText = findViewById(R.id.userName);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmPassword);
        phoneText = findViewById(R.id.phone);
        cityText = findViewById(R.id.city);
        countrySpinner = findViewById(R.id.country);
        userSelectGroup = findViewById(R.id.userSelect);
        userSelectGroup.check(R.id.general);
        tosAccept = findViewById(R.id.tosAccept);
        signupBtn = findViewById(R.id.btnSignup);
        signupBtn.setEnabled(false); //disabled by default, enabled after the tos has been checked
        signupBtn.setBackgroundColor(this.getResources().getColor(R.color.graycolor));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
        String currentCountry = this.getResources().getConfiguration().locale.getCountry();
        countrySpinner.setSelection(countries.indexOf(currentCountry));
        countrySpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //since we only have one adapter view, we can safely assume the parent is the countries spinner
        country = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    static class SignupPostTask extends AsyncTask<String,Void,String>{
        //the okhttp singleton
        OkHttpClient client=new OkHttpClient();
        private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private Exception exception;
        private String response;
        private String json;
        //the weak reference object to prevent memory leaks during garbage collection of signupActivity object
        private WeakReference<SignupActivity> signupActivityWeakReference;

        SignupPostTask(SignupActivity context)
        {
            signupActivityWeakReference = new WeakReference<>(context);
        }
        @Override
        protected String doInBackground(String... params) {

            String url = params[0];
            json = params[1];

            response = null;
            try {
                response = doPostRequest(url, json);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;


        }

        @Override
        protected void onPostExecute(String response) {
            System.out.println(response);
            if(response.equals("success"))
            {
                /* todo: after merging the login branch, uncomment this code to redirect to login
                //create intent to redirect to login page
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra(SignupActivity.EXTRA_USER,json); //the json object is passed as a string, which will be parsed on the other side
                signupActivityWeakReference.get().startActivity(intent);
                */
            }
            else
            {
                // todo: give an error to the user
            }
        }

        private String doPostRequest(String url, String json) throws IOException,NullPointerException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}


