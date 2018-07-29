package bizcom.bizcom;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {
    String userName;
    private Button btnUploadImageTest;
    private Button btnSelectImageTest;
    private ImageView imageView;
    private static final int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initialization;
        initializeVariables();
        btnUploadImageTest.setVisibility(View.GONE);

        //listeners
        btnSelectImageTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent = new Intent(Intent.ACTION_PICK);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                }
            }
        });
    }

    private void initializeVariables() {
        userName = getIntent().getStringExtra(SignupActivity.EXTRA_USERNAME);
        btnSelectImageTest = findViewById(R.id.btnSelectImageTest);
        btnUploadImageTest = findViewById(R.id.btnUploadImageTest);
        imageView = findViewById(R.id.imageViewTest);
    }
}
