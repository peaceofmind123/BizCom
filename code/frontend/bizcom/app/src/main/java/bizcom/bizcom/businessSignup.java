package bizcom.bizcom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class businessSignup extends AppCompatActivity implements View.OnClickListener{
    private Button  buttonChoose, buttonUpload;
    private ImageView imageView;

    private static final int STORAGE_PERMISSION_CODE=2342;
    private static final int PICK_IMAGE_REQUEST=22;
    private Uri filePath;
    private Bitmap bitmap;

    //address of webserver
   // private static final String UPLOAD_URL="http://192.168.0.1/uploadExample/upload.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);

        //requestStoragePermission();

        buttonUpload=(Button) findViewById(R.id.buttonUpload);
        buttonChoose=(Button) findViewById(R.id.buttonChoose);
        imageView=(ImageView) findViewById(R.id.profileImage);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }
    //Permission for uploading images
    private void requestStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            return;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    //permission handling
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"Permission not granted",Toast.LENGTH_LONG).show();
            }
        }
    }
    //show file for image
    private void showFileChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filePath= data.getData();
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);

            }catch (IOException e){

            }
        }
    }
    //return absolute path of images with uri object
    private String getPath(Uri uri){
        Cursor cursor =getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id =cursor.getString(0);

        document_id=document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor=getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID +"=?",
                new String[]{document_id},
                null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void uploadImage() {
        String name = editText.getText().toString().trim();
        String path=getPath(filePath);

        try{
            String uploadid= UUID.randomUUID().toString();

            new MultiPartUploadRequest(this,uploadid,UPLOAD_URL)
                    .addFileToUpload(path,"image")
                    .addParameter("name",name)
                    .setNotificationConfig(new UploadNotificataionConfig())
                    .setMaxRetries(2)
                    .startUpload();


        }catch (Exception e){

        }
    }
    @Override
    public void onClick(View view)
    {
        if(view==buttonUpload){
            uploadImage();
        }
        if(view==buttonChoose)
        {
            showFileChooser();
        }
    }
}
