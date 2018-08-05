package bizcom.bizcom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private ImageView profilePic, ad1,ad2,ad3,recAd1,recAd2,recAd3;
    private EditText searchEditText;
    private TextView hotspot, sponsored, recommendation,company1, company2,company3,recCompany1,recCompany2, recCompany3;
    private ImageButton btn_Search;
    private JSONObject user;
    private String userName;
    private ImageButton btnAddProfilePic;
    public static final int PICK_IMAGE = 1;
    private String company1Name;
    private String company2Name;
    private String company3Name;
    private String recCompany1Name;
    private String recCompany2Name;
    private String recCompany3Name;
    private ImageView searchResult1IV;
    private ImageView searchResult2IV;
    private ImageView searchResult3IV;
    private TextView searchResultsText;
    private HorizontalScrollView searchScrollView;
    private TextView searchResult1TV;
    private TextView searchResult2TV;
    private TextView searchResult3TV;
    private ArrayList<String> searchResultsCompanyName;
    private CardView searchCardView1;
    private CardView searchCardView2;
    private CardView searchCardView3;
    private boolean hasBeenSearched = false;
    private ScrollView mainScrollView;
    private int[] imageViewIDS = new int[]{R.id.iv_Ad1,R.id.iv_Ad2,R.id.iv_Ad3
            ,R.id.iv_SearchResult1,R.id.iv_SearchResult2,
            R.id.iv_SearchResult3,R.id.iv_RecAd1,R.id.iv_RecAd2,
            R.id.iv_RecAd3};
    private int[] textViewIDS = new int[] {R.id.tv_Company1,R.id.tv_Company2,
                                             R.id.tv_Company3,R.id.tv_SearchResult1,
                                            R.id.tv_SearchResult2,R.id.tv_SearchResult3,
                                            R.id.tv_RecCompany1,R.id.tv_RecCompany2,
                                            R.id.tv_RecCompany3};

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
        setContentView(R.layout.activity_home);
        initializeVariables();
        ImageNetworkHelper.requestPermissions(this);
        initializeView();

        btnAddProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfilePic();
            }
        });
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_Ad1);
            }
        });
        ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_Ad2);
            }
        });
        ad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_Ad3);
            }
        });
        recAd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_RecAd1);
            }
        });
        recAd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_RecAd2);
            }
        });
        recAd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_RecAd3);
            }
        });
        searchResult1IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_SearchResult1);
            }
        });
        searchResult2IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_SearchResult2);
            }
        });
        searchResult3IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(R.id.iv_SearchResult3);
            }
        });
    }

    private void handleImageClick(int id) {
        HashMap<Integer,Integer> ivToEtmap=new HashMap<>(9);
        for(int i =0;i<9;i++)
        {
            ivToEtmap.put(imageViewIDS[i],textViewIDS[i]);
        }
        TextView tv = findViewById(ivToEtmap.get(id));
        String companyName = tv.getText().toString();
        JSONObject userRequest = new JSONObject();
        try {
            userRequest.put("userName",companyName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(getString(R.string.urlBase))
                .appendPath("userAccounts")
                .appendPath("getUserDetails");
        JsonObjectRequest viewerGetRequest = new JsonObjectRequest(Request.Method.POST, builder.build().toString(), userRequest,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            String errorResponse = response.getString("userName");
                            Intent intent = new Intent(HomeActivity.this,ProfileActivity.class);
                            intent.putExtra(SignupActivity.EXTRA_USER_JSON,response.toString());
                            intent.putExtra(SignupActivity.EXTRA_VIEWER_JSON,user.toString());
                            HomeActivity.this.startActivity(intent);
                        }
                        catch (JSONException e)
                        {
                            //means an error
                           // DialogFragmentHelper.showDialogFragment(HomeActivity.this,R.string.err_server_dialogMsg);
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    DialogFragmentHelper.showDialogFragment(HomeActivity.this,R.string.err_server_nonresponsive_dialogMsg);
            }
        });
        MySingleton.getMinstance(this).addToRequestQueue(viewerGetRequest);


    }

    private void search() {
        String searchText = searchEditText.getText().toString();
        if(TextUtils.isEmpty(searchText))
            searchEditText.setError("Empty");
        else
        {
            searchCardView1.setVisibility(View.GONE);
            searchCardView2.setVisibility(View.GONE);
            searchCardView3.setVisibility(View.GONE);
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(this.getString(R.string.urlBase))
                    .appendPath("search")
                    .appendQueryParameter("query",searchText);

            Ion.with(this)
                    .load(builder.build().toString())
                    .asJsonArray()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonArray>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonArray> result) {
                            if(e!=null)
                            {
                                DialogFragmentHelper.showDialogFragment(HomeActivity.this,R.string.err_server_dialogMsg);
                            }
                            else
                            {
                                try
                                {
                                    handleSearchResult(result);
                                }
                                catch(NullPointerException e1)
                                {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    private void handleSearchResult(Response<JsonArray> companyNames) {

        searchResultsCompanyName = new ArrayList<>();
        int x = companyNames.getResult().size();
        System.out.println(x);
        if(!(companyNames.getResult().isJsonNull()))
        {
            for(int i=0;i<companyNames.getResult().size();i++)
            {
                searchResultsCompanyName.add(cleanCompanyName(companyNames.getResult().get(i).toString()));
            }
        }
        if(x==0)
        {
            //nothing found
            DialogFragmentHelper.showDialogFragment(this,"Nothing was found matching your query");
        }
        if(searchResultsCompanyName.size()>0)
        {
            searchResultsText.setVisibility(View.VISIBLE);
            searchScrollView.setVisibility(View.VISIBLE);
            searchCardView1.setVisibility(View.VISIBLE);
            searchResult1TV.setText(searchResultsCompanyName.get(0));
            ImageNetworkHelper.getProfilePhoto(this,searchResultsCompanyName.get(0),R.id.iv_SearchResult1);

        }

        if(searchResultsCompanyName.size()>1)
        {
            searchCardView2.setVisibility(View.VISIBLE);
            searchResult2TV.setText(searchResultsCompanyName.get(1));
            ImageNetworkHelper.getProfilePhoto(this,searchResultsCompanyName.get(1),R.id.iv_SearchResult2);
        }
        if(searchResultsCompanyName.size()>2)
        {
            searchResult3TV.setText(searchResultsCompanyName.get(2));
            searchCardView3.setVisibility(View.VISIBLE);
            ImageNetworkHelper.getProfilePhoto(this,searchResultsCompanyName.get(2),R.id.iv_SearchResult3);

        }





    }


    private void addProfilePic() {
        ImageNetworkHelper.selectImage(this,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data!=null)
        {
            Uri selectedImageUri = data.getData();
            String path = ImageNetworkHelper.getPathFromURI(this,selectedImageUri);
            if(requestCode==PICK_IMAGE)
            {
                profilePic.setImageURI(selectedImageUri);
            }
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(getString(R.string.urlBase))
                    .appendPath("profile")
                    .appendPath("uploadProfilePic");
            String url = builder.build().toString();
            ImageNetworkHelper.uploadImage(this,path,url,"userName",userName);
        }

    }

    private void initializeView()
    {

        getCompanyNames();

            ImageNetworkHelper.getProfilePhoto(this,userName,R.id.iv_ProfilePic);
        if(!TextUtils.isEmpty(company1Name))
            ImageNetworkHelper.getProfilePhoto(this,company1Name,R.id.iv_Ad1);
        if(!TextUtils.isEmpty(company2Name))
            ImageNetworkHelper.getProfilePhoto(this,company2Name,R.id.iv_Ad2);
        if(!TextUtils.isEmpty(recCompany3Name))
            ImageNetworkHelper.getProfilePhoto(this,company3Name,R.id.iv_Ad3);
        if(!TextUtils.isEmpty(recCompany1Name))
            ImageNetworkHelper.getProfilePhoto(this,recCompany1Name,R.id.iv_RecAd1);
        if(!TextUtils.isEmpty(recCompany2Name))
            ImageNetworkHelper.getProfilePhoto(this,recCompany2Name,R.id.iv_RecAd2);
        if(!TextUtils.isEmpty(recCompany3Name))
            ImageNetworkHelper.getProfilePhoto(this,recCompany3Name,R.id.iv_RecAd3);

    }

    private void getCompanyNames() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(this.getString(R.string.urlBase))
                .appendPath("home")
                .appendPath("getCompanyNames")
                .appendQueryParameter("userName",userName);
        Ion.with(this).load(builder.build().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(e!=null)
                        {
                            DialogFragmentHelper.showDialogFragment(HomeActivity.this,R.string.err_server);
                        }
                        else
                        {
                            handleCompanyNames(result);
                        }
                    }
                });
    }


    private String cleanCompanyName(String companyName)
    {
        if(!companyName.equals("null"))
        return companyName.substring(1,companyName.length()-1);
        else throw new NullPointerException("Empty String Recieved");
    }
    private void handleCompanyNames(JsonObject companies) {
        if(companies.get("response")==null)
        {
            try
            {
                company1Name = companies.get("company1").toString();
                company1Name = cleanCompanyName(company1Name);
                company1.setText(company1Name);
                ImageNetworkHelper.getProfilePhoto(this,company1Name,R.id.iv_Ad1);
            }
            catch(NullPointerException e)
            {

            }
            try
            {
                company2Name = companies.get("company2").toString();
                company2Name = cleanCompanyName(company2Name);
                company2.setText(company2Name);
                ImageNetworkHelper.getProfilePhoto(this,company2Name,R.id.iv_Ad2);
            }
            catch(NullPointerException e)
            {

            }
            try
            {
                company3Name = companies.get("company3").toString();
                company3Name = cleanCompanyName(company3Name);
                company3.setText(company3Name);
                ImageNetworkHelper.getProfilePhoto(this,company3Name,R.id.iv_Ad3);
            }
            catch(NullPointerException e)
            {

            }


            try
            {
                recCompany1Name = companies.get("recCompany1").toString();
                recCompany1Name = cleanCompanyName(recCompany1Name);
                recCompany1.setText(recCompany1Name);
                ImageNetworkHelper.getProfilePhoto(this,recCompany1Name,R.id.iv_RecAd1);
            }
            catch(NullPointerException e)
            {

            }
            try
            {
                recCompany2Name = companies.get("recCompany2").toString();
                recCompany2Name = cleanCompanyName(recCompany2Name);
                recCompany2.setText(recCompany2Name);
                ImageNetworkHelper.getProfilePhoto(this,recCompany2Name,R.id.iv_RecAd2);
            }
            catch(NullPointerException e)
            {

            }
            try
            {
                recCompany3Name = companies.get("recCompany3").toString();
                recCompany3Name = cleanCompanyName(recCompany3Name);
                recCompany3.setText(recCompany3Name);
                ImageNetworkHelper.getProfilePhoto(this,recCompany3Name,R.id.iv_RecAd3);
            }
            catch(NullPointerException e)
            {

            }

        }
        else
        {
            DialogFragmentHelper.showDialogFragment(this,R.string.err_server_dialogMsg);
        }
    }

    private void initializeVariables()
    {
        String userJson = getIntent().getStringExtra(SignupActivity.EXTRA_USER_JSON);
        try
        {
            user = new JSONObject(userJson);
            userName = user.getString("userName");
        }
        catch(JSONException e)
        {

        }
        searchResultsText = findViewById(R.id.tv_SearchResults);
        searchScrollView = findViewById(R.id.searchScrollView);
        searchResult1IV = findViewById(R.id.iv_SearchResult1);
        searchResult2IV = findViewById(R.id.iv_SearchResult2);
        searchResult3IV = findViewById(R.id.iv_SearchResult3);
        searchResult1TV = findViewById(R.id.tv_SearchResult1);
        searchResult2TV = findViewById(R.id.tv_SearchResult2);
        searchResult3TV = findViewById(R.id.tv_SearchResult3);
        searchCardView1 = findViewById(R.id.searchCardView1);
        searchCardView2 = findViewById(R.id.searchCardView2);
        searchCardView3 = findViewById(R.id.searchCardView3);
        mainScrollView = findViewById(R.id.mainScrollView);

        profilePic = findViewById(R.id.iv_ProfilePic);
        btnAddProfilePic = findViewById(R.id.btnAddProfilePic);
        ad1 = findViewById(R.id.iv_Ad1);
        ad2 = findViewById(R.id.iv_Ad2);
        ad3 = findViewById(R.id.iv_Ad3);

        recAd1 = findViewById(R.id.iv_RecAd1);
        recAd2 = findViewById(R.id.iv_RecAd2);
        recAd3 = findViewById(R.id.iv_RecAd3);
        searchEditText = findViewById(R.id.et_Search);

        hotspot = findViewById((R.id.tv_Hotspot));
        sponsored = findViewById(R.id.tv_Sponsored);
        recommendation = findViewById(R.id.tv_Recommend);

        company1= findViewById(R.id.tv_Company1);
        company2= findViewById(R.id.tv_Company2);
        company3= findViewById(R.id.tv_Company3);

        recCompany1 = findViewById(R.id.tv_RecCompany1);
        recCompany2 = findViewById(R.id.tv_RecCompany2);
        recCompany3 = findViewById(R.id.tv_RecCompany3);

        btn_Search = findViewById(R.id.btn_Search);

    }
}
