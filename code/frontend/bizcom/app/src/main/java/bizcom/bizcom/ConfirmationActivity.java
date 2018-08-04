package bizcom.bizcom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmationActivity extends AppCompatActivity{
    Button btnConfirm;
    EditText confirmationEditText;

    String email;
    String userName;
    String userType;
    ProgressBar progressBarConfirm;
    private String url;
    private String confirmationConfirmUrl;
    private String jsonStringUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        progressBarConfirm = findViewById(R.id.progressbarConfirmation);
        progressBarConfirm.setVisibility(View.GONE);

        email = getIntent().getStringExtra(SignupActivity.EXTRA_EMAIL);
        userName = getIntent().getStringExtra(SignupActivity.EXTRA_USERNAME);
        userType = getIntent().getStringExtra(SignupActivity.EXTRA_USERTYPE);
        jsonStringUser = getIntent().getStringExtra(SignupActivity.EXTRA_USER_JSON);
        sendConfirmationSMS();
        confirmationEditText = findViewById(R.id.confirmationCode);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.graycolor));
        confirmationConfirmUrl = getString(R.string.urlConfirmationConfirm);



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarConfirm.setVisibility(View.VISIBLE);
                if(!InternetCheckHelper.isInternetConnected(ConfirmationActivity.this))
                {
                    DialogFragmentHelper.showDialogFragment(ConfirmationActivity.this,R.string.dialog_internet_unavailable);
                    progressBarConfirm.setVisibility(View.GONE);
                }
                else
                {

                    new ConfirmationConfirmTask(ConfirmationActivity.this)
                            .execute(confirmationConfirmUrl,confirmationEditText.getText().toString(),userName);
                }
            }
        });
        confirmationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if(TextUtils.isEmpty(s.toString()))
                    {
                        btnConfirm.setEnabled(false);
                        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.graycolor));
                    }
                    else
                    {

                        btnConfirm.setEnabled(true);
                        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.colorPrimary));
                    }

            }
        });
    }

    private void sendConfirmationSMS() {
        url = getString(R.string.urlConfirmation);

        new ConfirmBackgroundTask(this).execute(url,email);
    }

    public void handleConfirmationResponse(String response)
    {
        progressBarConfirm.setVisibility(View.GONE);
        if(response.equals(getString(R.string.err_server_nonresponsive)) ||
                response.equals(getString(R.string.err_send_email)) ||
                response.equals(getString(R.string.err_server)))
        {

            AlertDialog.Builder serverErrorDialogBuilder = new AlertDialog.Builder(this);
            serverErrorDialogBuilder.setMessage(response);
            serverErrorDialogBuilder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ConfirmationActivity.this.recreate();
                }
            });
            AlertDialog alertDialog = serverErrorDialogBuilder.create();
            alertDialog.show();
        }

    }


    public void handleConfirmButtonPressedResponse(String response) { // when the user presses confirm
        System.out.println(response);
        HashMap<String,String> responseMap = mapResponses(); //maps responses with their dialog messages
        if(response.equals(getString(R.string.err_server)) ||
                response.equals(getString(R.string.err_general)) ||
                response.equals(getString(R.string.err_server_nonresponsive)) ||
                response.equals(getString(R.string.err_confirmation_incorrect)))
        {
            DialogFragmentHelper.showDialogFragment(this,responseMap.get(response));
        }
        else //means no errors
        {
            if(userType.equals("general")) //for generalUsers, we start the home activity
            {
                startHomeActivity();
            }
            else
            {
                startProfileActivity();
            }
        }
    }

    private HashMap<String,String> mapResponses() {
        HashMap<String,String> responseMap = new HashMap<>(4);
        responseMap.put(getString(R.string.err_server),getString(R.string.err_server_dialogMsg));
        responseMap.put(getString(R.string.err_general), getString(R.string.err_general_dialogMsg));
        responseMap.put(getString(R.string.err_server_nonresponsive),getString(R.string.err_server_nonresponsive_dialogMsg));
        responseMap.put(getString(R.string.err_confirmation_incorrect),getString(R.string.err_confirmation_incorrect_dialogMsg));
        return responseMap;
    }

    private void startProfileActivity() {
        Intent intent = new Intent(ConfirmationActivity.this, ProfileActivity.class);
        intent.putExtra(SignupActivity.EXTRA_USER_JSON,jsonStringUser);
        intent.putExtra(SignupActivity.EXTRA_VIEWER_JSON,jsonStringUser); //here the viewer is the same as the user
        startActivity(intent);
    }

    private void startHomeActivity() {
        Intent intent = new Intent(ConfirmationActivity.this,HomeActivity.class);
        intent.putExtra(SignupActivity.EXTRA_USER_JSON,jsonStringUser);
        startActivity(intent);
    }

}

class ConfirmBackgroundTask extends AsyncTask<String,Void,String>{
    OkHttpClient client=new OkHttpClient();
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private WeakReference<ConfirmationActivity> confirmationActivityWeakReference;


    public ConfirmBackgroundTask(ConfirmationActivity context)
    {
        confirmationActivityWeakReference = new WeakReference<ConfirmationActivity>(context);

    }
    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String email = strings[1];

        String response=null;
        JSONObject jsonObject = new JSONObject();
        ConfirmationActivity confirmationActivity;
        try
        {
            jsonObject.put("email",email);
            RequestBody body = RequestBody.create(JSON,jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().string();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            confirmationActivity = confirmationActivityWeakReference.get();
            response = confirmationActivity.getString(R.string.err_general);
        }
        catch(IOException e) //means that the call wasn't responded
        {
            confirmationActivity = confirmationActivityWeakReference.get();
            response = confirmationActivity.getString(R.string.err_server_nonresponsive);

        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
            confirmationActivityWeakReference.get().handleConfirmationResponse(response);
    }

}

class ConfirmationConfirmTask extends AsyncTask<String,Void,String>{
    OkHttpClient client=new OkHttpClient();
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private WeakReference<ConfirmationActivity> confirmationActivityWeakReference;
    private String confirmationCode,url,response,userName;

    public ConfirmationConfirmTask(ConfirmationActivity context)
    {
        confirmationActivityWeakReference = new WeakReference<ConfirmationActivity>(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        url = strings[0];
        confirmationCode = strings[1];
        userName = strings[2];
        JSONObject jsonObject = new JSONObject();
        ConfirmationActivity confirmationActivity = confirmationActivityWeakReference.get();
        try
        {
            jsonObject.put("userName",userName);
            jsonObject.put("confirmationCode", confirmationCode);
            RequestBody body = RequestBody.create(JSON,jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().string();
        }
        catch (IOException e) //server didn't respond
        {

            response = confirmationActivity.getString(R.string.err_server_nonresponsive);

        }
        catch(JSONException e)
        {
            response = confirmationActivity.getString(R.string.err_general);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        confirmationActivityWeakReference.get().handleConfirmButtonPressedResponse(s);
    }
}