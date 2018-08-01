package bizcom.bizcom;

import android.annotation.SuppressLint;
import android.content.CursorLoader;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePic, companyAdPic;
    private ImageButton btn_AddUserImage, btn_EditUsername, btn_EditDescription, btn_EditAddInfo, btn_AddBigImg, btn_AddNext;
    private EditText companyName, description;
    private Button btn_Update;
    private TextView additionalInfo;
    private RatingBar ratingBar;
    private String selectedImagePath;
    private String userType;
    private String userJson;
    private JSONObject user;
    private String userName;
    private RelativeLayout addImageNextLayout;
    private List<ImageView> adImagesNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageNetworkHelper.requestPermissions(this);
        setContentView(R.layout.activity_profile);
        initializeVariables();
        initializeView();
        btn_AddUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(PICK_PROFILE_PIC);
            }
        });

        btn_AddBigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(PICK_AD_PIC);
            }
        });
        btn_AddNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(PICK_NEXT_PIC);
            }
        });
    }







    private void initializeView() {
        //if the usertype is user,nothing can be edited except the rating bar

        if(userType.equals("general")){

            btn_AddBigImg.setVisibility(View.GONE);
            btn_Update.setVisibility(View.GONE);
            btn_AddNext.setVisibility(View.GONE);
            companyName.setEnabled(false);
            description.setEnabled(false);
            additionalInfo.setEnabled(false);
            if(companyAdPic.getDrawable()==null){
                companyAdPic.setVisibility(View.GONE);

            }

        }
        //if usertype  is not a general user
        else{
            ratingBar.setFocusable(false);
        }






        //if user is a companyfirm holder
        //if(usertype == ..){
        //
        // }
    }

    private void initializeVariables() {
        adImagesNext = new ArrayList<ImageView>();
        addImageNextLayout = findViewById(R.id.adImageNextLayout);
        userJson = getIntent().getStringExtra(SignupActivity.EXTRA_USER_JSON);
        try {
            user = new JSONObject(userJson);
            userType = user.getString("userType");
            userName = user.getString("userName");
        } catch (JSONException e) {
            DialogFragmentHelper.showDialogFragment(this,R.string.err_general_dialogMsg);
            e.printStackTrace();
        }
        profilePic = findViewById(R.id.iv_profilePic);
        companyAdPic = findViewById(R.id.iv_BigImg);
        btn_AddUserImage = findViewById(R.id.btn_UserImg);

        btn_AddBigImg = findViewById(R.id.btn_AddBigImg);
        btn_AddNext = findViewById(R.id.btn_AddNext);
        companyName = findViewById(R.id.et_CompanyName);
        description = findViewById(R.id.et_Description);
        additionalInfo = findViewById(R.id.et_AdditonalInfo);
        btn_Update = findViewById((R.id.btn_Update));
        ratingBar=findViewById(R.id.ratingBar);
    }



    //selecting image from gallery

    public void selectImage(int request){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, request);

    }
    public static final int PICK_PROFILE_PIC =1; //this is for comparision with the request code
    public static final int PICK_AD_PIC=2;
    public static final int PICK_NEXT_PIC=3;


    //getpathmethod

    private String getPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri.Builder builder = new Uri.Builder();
        if(resultCode == RESULT_OK && data!=null){
            Uri selectedImageUri = data.getData();
            selectedImagePath =  getPathFromURI(selectedImageUri);
            if(requestCode == PICK_PROFILE_PIC)
            {
                profilePic.setImageURI(selectedImageUri);
                builder.scheme("http")
                        .encodedAuthority(getString(R.string.urlBase))
                        .appendPath("profile")
                        .appendPath("uploadProfilePic");
                String url = builder.build().toString();
                ImageNetworkHelper.uploadImage(this,selectedImagePath,url,"userName",userName);

            }
            else if (requestCode == PICK_AD_PIC)
            {
                companyAdPic.setImageURI(selectedImageUri);
                builder.scheme("http")
                        .encodedAuthority(getString(R.string.urlBase))
                        .appendPath("profile")
                        .appendPath("uploadAdPic")
                        .appendQueryParameter("userName",userName);
                String url = builder.build().toString();
                ImageNetworkHelper.uploadImage(this,selectedImagePath,url,"userName",userName);

            }
            else
            {
                /*todo: add next pick logic*/
                RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                ImageView imgViewNew=new ImageView(this);
                imgViewNew.setLayoutParams(lparams);
                adImagesNext.add(imgViewNew);
                imgViewNew.setImageURI(selectedImageUri);
                this.addImageNextLayout.addView(imgViewNew);
            }



        }



    }
}


