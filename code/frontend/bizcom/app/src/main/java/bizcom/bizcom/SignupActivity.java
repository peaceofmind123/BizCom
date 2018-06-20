package bizcom.bizcom;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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

    public void signUp(View view) {
        fName = fNameText.getText().toString();
        lName = lNameText.getText().toString();
        userName = userNameText.getText().toString();
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();
        phone = phoneText.getText().toString();
        city = cityText.getText().toString();



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

        signupBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                awesomeValidation.validate();
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
}
