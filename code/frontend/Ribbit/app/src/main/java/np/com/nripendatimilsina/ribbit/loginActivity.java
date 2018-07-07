package np.com.nripendatimilsina.ribbit;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class loginActivity extends AppCompatActivity {

    protected TextView mSighUpTextView;

    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mloginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSighUpTextView= (TextView)findViewById(R.id.SignUpText);//on click on signuptext id
        mSighUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // from login page to signup page
                Intent intent = new Intent(loginActivity.this,signUpActivity.class);
                startActivity(intent);
            }
        });

        mUsername =(EditText)findViewById(R.id.userNameField);
        mPassword =(EditText)findViewById(R.id.passwordField);

        mloginButton=(Button)findViewById(R.id.signupButton);
        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=mUsername.getText().toString();
                String password=mPassword.getText().toString();

                username=username.trim();
                password=password.trim();

                if(username.isEmpty()||password.isEmpty())
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
