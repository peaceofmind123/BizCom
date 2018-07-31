package bizcom.bizcom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView profilePic, companyAdPic;
    private ImageButton btn_AddUserImage, btn_EditUsername, btn_EditDescription, btn_EditAddInfo, btn_AddCompanyImg, btn_AddNext;
    private EditText companyName, description;
    private Button btn_Update;
    private TextView additionalInfo;
    private RatingBar ratingBar;
    private String selectedImagePath;
    private String userType="user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeVariables();
        initializeView();

    }

    private void initializeView() {
        //if the usertype is user,nothing can be edited except the rating bar
        if(userType.equals("general")){
            btn_AddUserImage.setVisibility(View.GONE);
            btn_AddCompanyImg.setVisibility(View.GONE);
            btn_Update.setVisibility(View.GONE);
            btn_AddNext.setVisibility(View.GONE);
            companyName.setInputType(InputType.TYPE_NULL);
            description.setInputType(InputType.TYPE_NULL);
            additionalInfo.setInputType(InputType.TYPE_NULL);

        }
        //if usertype  is not a general user
        else{
            ratingBar.setFocusable(false);
        }

        //if there is no advertising image at the start the image view will be set to gone

        if(companyAdPic.getDrawable()!=null){
            System.out.println("error");
        }
        else{
            companyAdPic.setVisibility(View.GONE);
            btn_AddCompanyImg.setVisibility(View.GONE);
        }


        //if user is a companyfirm holder
        //if(usertype == ..){
        //
        // }
    }

    private void initializeVariables() {
        profilePic = findViewById(R.id.iv_profilePic);
        companyAdPic = findViewById(R.id.iv_BigImg);
        btn_AddUserImage = findViewById(R.id.btn_UserImg);

        btn_AddCompanyImg = findViewById(R.id.btn_AddBigImg);
        btn_AddNext = findViewById(R.id.btn_AddNext);
        companyName = findViewById(R.id.et_CompanyName);
        description = findViewById(R.id.et_Description);
        additionalInfo = findViewById(R.id.et_AdditonalInfo);
        btn_Update = findViewById((R.id.btn_Update));
        ratingBar=findViewById(R.id.ratingBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_UserImg:
                selectImage();


                break;


            case R.id.btn_AddBigImg:
                selectImage();

                break;

            case R.id.btn_AddNext:
                companyAdPic.setVisibility(View.VISIBLE);
                btn_AddCompanyImg.setVisibility(View.GONE);
                break;


        }
    }

    //selecting image from gallery

    public void selectImage(){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);

    }
    public static final int PICK_IMAGE =1; //this is for comparision with the request code


    //getpathmethod
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK && null!=data){

            Uri selectedImageUri = data.getData();
            selectedImagePath =  getPath(selectedImageUri);
            System.out.println("Image Path: "+ selectedImagePath);
            profilePic.setImageURI(selectedImageUri);

        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}


