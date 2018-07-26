package bizcom.bizcom;

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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordEnterForgotCode extends AppCompatActivity {

    private ProgressBar forgotPassCodeProgressBar;
    private Button resetPasswordBtn;
    private EditText forgotPassCodeEditText;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_enter_forgot_code);

        //initialization
        forgotPassCodeProgressBar = findViewById(R.id.forgotPassCodeProgressBar);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        forgotPassCodeEditText = findViewById(R.id.forgotPassCodeText);

        //initial setup
        forgotPassCodeProgressBar.setVisibility(View.GONE);
        resetPasswordBtn.setEnabled(false);
        resetPasswordBtn.setBackgroundColor(getResources().getColor(R.color.graycolor));

        //validation of code: only 5 or 6 digit code allowed
        awesomeValidation.addValidation(this,R.id.forgotPassCodeText,"^[0-9]{5,6}$",R.id.err_invalid_code);

        //listeners
        forgotPassCodeEditText.addTextChangedListener(new TextWatcher() {
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
                        resetPasswordBtn.setBackgroundColor(getResources().getColor(R.color.graycolor));
                        resetPasswordBtn.setEnabled(false);
                    }
                    else
                    {
                        resetPasswordBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        resetPasswordBtn.setEnabled(true);
                    }
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    forgotPassCodeProgressBar.setVisibility(View.VISIBLE);
                    if(InternetCheckHelper.isInternetConnected(ForgotPasswordEnterForgotCode.this))
                    {
                        String email = getIntent().getStringExtra(ForgotPasswordActivity.EXTRA_FORGOT_EMAIL);
                        new SendForgotCodeTask(ForgotPasswordEnterForgotCode.this).execute(getString(R.string.urlforgotPasswordSendCode),email);
                    }

                    else
                    {
                        DialogFragmentHelper.showDialogFragment(ForgotPasswordEnterForgotCode.this,R.string.dialog_internet_unavailable);
                    }
                }


            }
        });
    }

    public void handleResponse(String response) {
        /*todo: handle the response*/
        System.out.println(response);
    }
}

class SendForgotCodeTask extends AsyncTask<String,Void,String>
{
    private WeakReference<ForgotPasswordEnterForgotCode> forgotPasswordEnterForgotCodeWeakReference;
    private OkHttpClient client = new OkHttpClient();

    SendForgotCodeTask(ForgotPasswordEnterForgotCode context)
    {
        forgotPasswordEnterForgotCodeWeakReference = new WeakReference<ForgotPasswordEnterForgotCode>(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String email = strings[1];
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
            response = forgotPasswordEnterForgotCodeWeakReference.get().getString(R.string.err_general);
        }
        catch (IOException e)
        {
            response = forgotPasswordEnterForgotCodeWeakReference.get().getString(R.string.err_server);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        forgotPasswordEnterForgotCodeWeakReference.get().handleResponse(response);
    }
}
