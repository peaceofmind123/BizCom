package bizcom.bizcom;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button forgotPasswordBtn;
    private EditText forgotPasswordEmail;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setBackgroundColor(getResources().getColor(R.color.graycolor));
        forgotPasswordBtn.setEnabled(false);

        forgotPasswordEmail = findViewById(R.id.forgotPasswordEmail);

        forgotPasswordEmail.addTextChangedListener(new TextWatcher() {
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
                    forgotPasswordBtn.setBackgroundColor(getResources().getColor(R.color.graycolor));
                    forgotPasswordBtn.setEnabled(false);
                }
                else
                {
                    forgotPasswordBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    forgotPasswordBtn.setEnabled(true);
                }
            }
        });

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.forgotPasswordEmail, Patterns.EMAIL_ADDRESS,R.string.err_email);

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    if(InternetCheckHelper.isInternetConnected(ForgotPasswordActivity.this))
                    {

                    }
                    else
                    {
                        DialogFragmentHelper.showDialogFragment(ForgotPasswordActivity.this,R.string.dialog_internet_unavailable);
                    }
                }
            }
        });
    }

    public void handleResponse(String response) {
        System.out.println(response);
        if(response.equals(getString(R.string.success)))
        {
            /*todo: send the password reset code to the email, then redirect to new activity to handle it*/
        }
        else
        {
            HashMap<String, String> resourceMap = mapResources();
            try
            {
                DialogFragmentHelper.showDialogFragment(this,resourceMap.get(response));
            }
            catch(Exception e) //if unexpected response is found
            {
                e.printStackTrace();
                DialogFragmentHelper.showDialogFragment(this,getString(R.string.err_general_dialogMsg));
            }

        }
    }

    private HashMap<String, String> mapResources() {
        HashMap<String,String> resourceMap = new HashMap<>(4);
        resourceMap.put(getString(R.string.err_general),getString(R.string.err_general_dialogMsg));
        resourceMap.put(getString(R.string.err_server_nonresponsive),getString(R.string.err_server_nonresponsive_dialogMsg));
        resourceMap.put(getString(R.string.err_general),getString(R.string.err_general_dialogMsg));
        resourceMap.put(getString(R.string.err_general),getString(R.string.err_general_dialogMsg));
        return resourceMap;
    }
}

class ForgotPasswordSendEmail extends AsyncTask<String,Void,String>{
    private WeakReference<ForgotPasswordActivity> forgotPasswordActivityWeakReference;
    private OkHttpClient client;

    ForgotPasswordSendEmail(ForgotPasswordActivity context)
    {
        forgotPasswordActivityWeakReference = new WeakReference<ForgotPasswordActivity>(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String email = strings[1];
        client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String response;
        JSONObject jsonObject = new JSONObject();
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
            response = forgotPasswordActivityWeakReference.get().getString(R.string.err_general);
        }
        catch (IOException e)
        {
            response = forgotPasswordActivityWeakReference.get().getString(R.string.err_server);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        forgotPasswordActivityWeakReference.get().handleResponse(response);
    }
}
