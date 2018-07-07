package np.com.nripendatimilsina.ribbit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signUpActivity extends AppCompatActivity {


    protected EditText mUsername;
    protected EditText mPassword;
    protected EditText mEmail;
    protected Button msignUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsername =(EditText)findViewById(R.id.userNameField);
        mPassword =(EditText)findViewById(R.id.passwordField);
        mEmail =(EditText)findViewById(R.id.EmailField);
        msignUpButton=(Button)findViewById(R.id.signupButton);
        msignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=mUsername.getText().toString();
                String password=mPassword.getText().toString();
                String email=mEmail.getText().toString();

                username=username.trim();
                password=password.trim();
                email=email.trim();
                if(username.isEmpty()||password.isEmpty()||email.isEmpty())
                {
                    //error message


                }
                else
                {
                    // create the new user!
                }

            }
        });


    }
}
