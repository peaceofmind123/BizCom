package bizcom.bizcom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity  {
    private static final int REQUEST_DELETE_THEN_UPLOAD = 90802;
    private int extraAdSelectedImageViewID;
    private int addedImages=0;
    private static final int SELECT_REQUEST_EXTRAAD = 2;
    private ImageView companyLogo, companyAdPic;
    private ImageButton btn_addCompanyLogo,btn_AddNext,btn_AddCompanyAd;
    private Button btn_Save;
    private EditText  additionalInfo;
    private TextView companyName;
    private RatingBar ratingBar;
    private LinearLayout finalLinearLayout;
    private CardView cardView;

    private ArrayList<CardView> cards;
    private ArrayList<ImageButton> buttons;
    private ArrayList<ImageView> images;
    private String userJson;
    private JSONObject user;
    private static final int SELECT_REQUEST = 1;
    private static final int SELECT_MAINAD_REQUEST=3;
    private String userName;
    private JSONObject viewerUser;
    private String additionalInfoString;
    private ProgressBar addInfoProgressBar;
    private CardView finalCardview;

    private void ifNormalUser(){


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.settings,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logoutMenuItem:
                BizcomHelperClass.logout(this,user);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeVariables();
        initializeView();
        ImageNetworkHelper.requestPermissions(this);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalInfoString = additionalInfo.getText().toString();
                saveAdditionalInfo();
            }
        });
        btn_addCompanyLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }


        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                postRating(rating);
            }
        });







        btn_AddNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_AddNext.setVisibility(View.GONE);
                addNextImageView();
            }
        });

        btn_AddCompanyAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCompanyAd();
            }
        });




//        imageView.setImageResource(R.drawable.blurroad);
//        imageView.setBackgroundResource(R.drawable.saverounded);
//        imageView.setVisibility(View.VISIBLE);
//        linearLayout.addView(imageView);
    }

    private void postRating(float rating) {
        JSONObject jsonObject  = new JSONObject();
        try {
            jsonObject.put("userName",user.getString("userName"));
            jsonObject.put("rating",rating);
            jsonObject.put("viewerUserName",viewerUser.getString("userName"));
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(getString(R.string.urlBase))
                    .appendPath("profile")
                    .appendPath("postRating");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, builder.build().toString(), jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("response").equals("error"))
                                {
                                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_general_dialogMsg);
                                }
                                else if(response.getString("response").equals("not logged in"))
                                {
                                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.not_loggged_in_dialogMsg);
                                }
                                else if(response.getString("response").equals("success"))
                                {

                                }
                                else if(response.getString("response").equals("user not found"))
                                {
                                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.not_loggged_in_dialogMsg);
                                }
                                else
                                {
                                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_general_dialogMsg);
                                }
                            }
                            catch(JSONException e)
                            {
                                DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_dialogMsg);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_nonresponsive_dialogMsg);
                }
            });
            MySingleton.getMinstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addCompanyAd() {
        ImageNetworkHelper.selectImage(this,SELECT_MAINAD_REQUEST);
    }

    private void saveAdditionalInfo() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(getString(R.string.urlBase))
                .appendPath("profile")
                .appendPath("updateAdditionalInfo");
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("additionalInfo",additionalInfoString);
            jsonObject.put("userName",userName);
            jsonObject.put("loginToken",user.getString("loginToken"));

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, builder.build().toString(), jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                addInfoProgressBar.setVisibility(View.GONE);
                try
                {
                    String res = response.getString("response");
                    if(!res.equals("success"))
                    {
                        DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_dialogMsg);
                    }
                }
                catch(JSONException e)
                {
                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_dialogMsg);
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_nonresponsive_dialogMsg);
            }
        });
        MySingleton.getMinstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void initializeView() {
        getMainAdPic();
        getExtraAdPics();
        hideRatingForBusinessUser();
        getRatings();
        try
        {
            String viewerUserType=viewerUser.getString("userType");
            if(viewerUserType.equals("general"))
            {
                disableEditWidgets();
            }
        }
        catch(JSONException e)
        {
            DialogFragmentHelper.showDialogFragment(this,"An error occoured, please try again");
            disableEditWidgets();
        }
        ImageNetworkHelper.getProfilePhoto(this,userName,R.id.iv_CompanyLogo);
        companyName.setText(userName);
        try
        {
            additionalInfoString = user.getString("additionalInfo");
            additionalInfo.setText(additionalInfoString);
        }
        catch (JSONException e)
        {

        }
    }

    private void getRatings() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(getString(R.string.urlBase))
                .appendPath("profile")
                .appendPath("getRatings")
        .appendQueryParameter("userName",userName);



            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        try
                        {
                            float rating = (float) response.getDouble("rating");
                            ratingBar.setRating(rating);
                        }
                        catch (JSONException e)
                        {
                            DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_dialogMsg);
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,R.string.err_server_nonresponsive_dialogMsg);
                }
            });
            MySingleton.getMinstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void hideRatingForBusinessUser() {
        try
        {
            String viewerUserType = viewerUser.getString("userType");
            if(viewerUserType.equals("business"))
            {
                ratingBar.setEnabled(false);
                ratingBar.setVisibility(View.GONE);

            }
        }
        catch(JSONException e)
        {

        }

    }

    private void getMainAdPic() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(getString(R.string.urlBase))
                .appendPath("profile")
                .appendPath("getMainAdPic")
                .appendQueryParameter("userName",userName);
        ImageNetworkHelper.downloadImage(this,builder.build().toString(),
                R.id.iv_CompanyAd,R.drawable.emptyimage,R.drawable.emptyimage);
    }

    private void getExtraAdPics() {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                .encodedAuthority(getString(R.string.urlBase))
                .appendPath("profile")
                .appendPath("getExtraAdPicNo")
                .appendQueryParameter("userName",userName);

            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("userName",userName);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                                try
                                {
                                    int noOfExtraAds = response.getInt("number");
                                    handleExtraAdPhotos(noOfExtraAds);
                                }
                                catch (JSONException e)
                                {
                                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,"Error retrieving ads, please reload to continue");
                                }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    DialogFragmentHelper.showDialogFragment(ProfileActivity.this,"Error retrieving ads, please reload to continue");
                }
            });
            MySingleton.getMinstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void handleExtraAdPhotos(int noOfExtraAds) {

        for(int i=0;i<noOfExtraAds;i++)
        {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(getString(R.string.urlBase))
                    .appendPath("profile")
                    .appendPath("getExtraAdPic")
                    .appendQueryParameter("userName",userName)
                    .appendQueryParameter("number",Integer.toString(i));
            addNextImageView();
            ImageNetworkHelper.downloadImage(this,builder.build().toString(),images.get(images.size()-1).getId(),R.drawable.emptyimage,R.drawable.emptyimage);
        }
    }

    private void disableEditWidgets() {

        try {
            String additionalInformation = user.getString("additionalInfo");
            System.out.println(additionalInformation);
            System.out.println(additionalInformation.matches(""));
            if (additionalInformation.isEmpty())        {
                cardView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(companyAdPic.getDrawable()==null)
        {
            finalCardview.setVisibility(View.GONE);
        }
            btn_addCompanyLogo.setVisibility(View.INVISIBLE);
            btn_addCompanyLogo.setEnabled(false);
            btn_AddCompanyAd.setVisibility(View.INVISIBLE);
            btn_AddCompanyAd.setEnabled(false);
            btn_AddNext.setVisibility(View.GONE);
            btn_AddNext.setEnabled(false);
            additionalInfo.setEnabled(false);
            additionalInfo.setTextColor(getResources().getColor(android.R.color.black));
            btn_Save.setEnabled(false);
            btn_Save.setVisibility(View.INVISIBLE);
    }


    private void initializeVariables(){

        companyLogo=findViewById(R.id.iv_CompanyLogo);
        companyAdPic=findViewById(R.id.iv_CompanyAd);

        btn_addCompanyLogo=findViewById(R.id.btn_AddCompanyLogo);
        btn_AddCompanyAd=findViewById(R.id.btn_AddCompanyAd);
        btn_AddNext=findViewById(R.id.btn_AddNext);

        btn_Save=findViewById(R.id.btn_Save);
        cardView = findViewById(R.id.cardView);
        companyName=findViewById(R.id.tv_CompanyName);
        additionalInfo=findViewById(R.id.et_AdditionalInfo);

        ratingBar=findViewById(R.id.ratingBar);
        finalCardview = findViewById(R.id.finalCardView);
        addInfoProgressBar = findViewById(R.id.addInfoProgress);
        cards = new ArrayList<CardView>();

        buttons = new ArrayList<ImageButton>();

        images = new ArrayList<ImageView>();
        userJson = getIntent().getStringExtra(SignupActivity.EXTRA_USER_JSON);
        String viewerUserString = getIntent().getStringExtra(SignupActivity.EXTRA_VIEWER_JSON);

        try {
            user = new JSONObject(userJson);
            viewerUser = new JSONObject(viewerUserString);
            userName = user.getString("userName");
        } catch (JSONException e) {
            DialogFragmentHelper.showDialogFragment(this,R.string.err_server_dialogMsg);
        }


    }

    private void selectImage(){

        ImageNetworkHelper.selectImage(this,SELECT_REQUEST);

        btn_AddNext.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK &&data!=null)
        {
            Uri selectedImageUri = data.getData();
            String path = ImageNetworkHelper.getPathFromURI(this,selectedImageUri);
            if(requestCode==SELECT_REQUEST)
            {
                companyLogo.setImageURI(selectedImageUri);
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .encodedAuthority(getString(R.string.urlBase))
                        .appendPath("profile")
                        .appendPath("uploadProfilePic");
                ImageNetworkHelper.uploadImage(this,path,builder.build().toString(),"userName",userName);
            }
            else if(requestCode==SELECT_REQUEST_EXTRAAD)
            {
                images.get(extraAdSelectedImageViewID-1).setImageURI(selectedImageUri);
                Uri.Builder builder = new Uri.Builder();
                ImageView imageView = findViewById(extraAdSelectedImageViewID);

                if(images.size()==extraAdSelectedImageViewID && imageView.getDrawable() ==null)
                {
                    builder.scheme("http")
                            .encodedAuthority(getString(R.string.urlBase))
                            .appendPath("profile")
                            .appendPath("uploadExtraAdPic");
                    ImageNetworkHelper.uploadImage(this,path,builder.build().toString(),"userName",userName,"number",Integer.toString(extraAdSelectedImageViewID-1));
                }
                else
                {
                    builder.scheme("http")
                            .encodedAuthority(getString(R.string.urlBase))
                            .appendPath("profile")
                            .appendPath("deleteExtraAdPic");
                    ImageNetworkHelper.deleteImage(this,path,builder.build().toString(),"userName",userName,"number",Integer.toString(extraAdSelectedImageViewID-1),REQUEST_DELETE_THEN_UPLOAD);
                }

            }
            else if(requestCode==SELECT_MAINAD_REQUEST)
            {
                companyAdPic.setImageURI(selectedImageUri);
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .encodedAuthority(getString(R.string.urlBase))
                        .appendPath("profile")
                        .appendPath("uploadAdPic");
                ImageNetworkHelper.uploadImage(this,path,builder.build().toString(),"userName",userName);
            }
        }
    }

    private void addNextImageView(){
        addedImages++;
        //pointer to the existing linear layout
        finalLinearLayout = findViewById(R.id.finalLinearLayout);

        //adding new cardview
        CardView cardView = new CardView(ProfileActivity.this);


        //parameteres for new cardview
        LinearLayout.LayoutParams lb = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lb.bottomMargin=6;
        cardView.setLayoutParams(lb);
        cardView.setContentPadding(4,4,4,4);







        //for image addition in card
        final float scale = this.getResources().getDisplayMetrics().density;
//        int imagewidth = (int) (450*scale + 0.5f);
//        int imageheight = (int)(300*scale + 0.5f);
        ImageView imageView = new ImageView(ProfileActivity.this);
        imageView.setId(addedImages);
        CardView.LayoutParams cardParamsForImage = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        cardParamsForImage.bottomMargin=4;





        //for button addition in card
        ImageButton btn_AddNewCompanyAd = new ImageButton(this);


        CardView.LayoutParams cardParamsForButton = new CardView.LayoutParams(70,70);
        try {
            if (viewerUser.getString("userType").equals("general")){

                btn_AddNewCompanyAd.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //give image the parameteres
        imageView.setLayoutParams(cardParamsForImage);

        //give button the parameteres
        btn_AddNewCompanyAd.setLayoutParams(cardParamsForButton);

        //addtional properties for imageview
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setBackgroundResource(R.drawable.edittextrounded);


        //addtional properties for button
        btn_AddNewCompanyAd.setBackgroundResource(R.drawable.saverounded);
        btn_AddNewCompanyAd.setImageResource(R.drawable.addphoto1);


        //add imageview to cardview
        cardView.addView(imageView);

        //add button to cardview
        cardView.addView(btn_AddNewCompanyAd);

        finalLinearLayout.addView(cardView);


        cards.add(cardView);

        images.add(imageView);

        buttons.add(btn_AddNewCompanyAd);

        final int currentID = buttons.size();
        buttons.get(buttons.size()-1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                extraAdSelectedImageViewID = currentID;
                ImageNetworkHelper.selectImage(ProfileActivity.this,SELECT_REQUEST_EXTRAAD);
                btn_AddNext.setVisibility(View.VISIBLE);
                //bizcom.bizcom.ImageNetworkHelper.uploadImage(MainActivity.this,);

            }
        });




    }

    private void handleExtraAd() {
        ImageNetworkHelper.selectImage(this,SELECT_REQUEST_EXTRAAD);

    }


    private void updateCompanyName() {

    }


    public void hasBeenDeleted(int requestNo,String path) {
        if(requestNo == REQUEST_DELETE_THEN_UPLOAD)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(getString(R.string.urlBase))
                    .appendPath("profile")
                    .appendPath("uploadExtraAdPic");
            ImageNetworkHelper.uploadImage(this,path,builder.build().toString(),"userName",userName,"number",Integer.toString(extraAdSelectedImageViewID-1));

        }
    }
}


