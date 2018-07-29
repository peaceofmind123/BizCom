package bizcom.bizcom;

import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.media.Image;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;

public class ProfileActivity extends AppCompatActivity {
    String userName;
    String path;
    
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
                pickPhoto();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getPathFromURI(data.getData());
                    imageView.setImageURI(data.getData());
                    btnUploadImageTest.setVisibility(View.VISIBLE);

                }
        }
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void pickPhoto() {
        Intent fintent = new Intent(Intent.ACTION_PICK);
        fintent.setType("image/jpeg");
        try {
            startActivityForResult(fintent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeVariables() {
        userName = getIntent().getStringExtra(SignupActivity.EXTRA_USERNAME);
        btnSelectImageTest = findViewById(R.id.btnSelectImageTest);
        btnUploadImageTest = findViewById(R.id.btnUploadImageTest);
        imageView = findViewById(R.id.imageViewTest);
    }
}
