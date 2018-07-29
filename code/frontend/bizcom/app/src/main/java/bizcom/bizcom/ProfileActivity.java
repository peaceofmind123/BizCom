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
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Future;
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
        btnUploadImageTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


    }
    private void initializeVariables() {
        userName = getIntent().getStringExtra(SignupActivity.EXTRA_USERNAME);
        btnSelectImageTest = findViewById(R.id.btnSelectImageTest);
        btnUploadImageTest = findViewById(R.id.btnUploadImageTest);
        imageView = findViewById(R.id.imageViewTest);
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

    private void uploadImage() {
        File f = new File(path);

        Future uploading = Ion.with(ProfileActivity.this)
                .load(getString(R.string.urlImageUpload))
                .setMultipartFile("image", f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        try {
                            JSONObject jobj = new JSONObject(result.getResult());
                            Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
    }





    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
