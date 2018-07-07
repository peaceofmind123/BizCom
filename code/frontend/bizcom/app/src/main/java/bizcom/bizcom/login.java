package bizcom.bizcom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity  implements View.OnClickListener{

    protected TextView msignUpTextView;
    Button blogin;
    EditText etUsername, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername= findViewById(R.id.login_userName);
        etPassword= findViewById(R.id.login_password);
        blogin= findViewById(R.id.login_button);
        blogin.setOnClickListener(this);






        msignUpTextView= findViewById(R.id.signUpText);
        msignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(login.this, Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){


        }

    }
}
