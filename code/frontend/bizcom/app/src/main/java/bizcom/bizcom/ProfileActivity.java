package bizcom.bizcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialization;
        initializeVariables();
    }

    private void initializeVariables() {
        userName = getIntent().getStringExtra(SignupActivity.EXTRA_USERNAME);
    }
}
