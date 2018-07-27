package bizcom.bizcom;

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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordNewPasswordActivity extends AppCompatActivity {

    private EditText resetPasswordPassText;
    private EditText resetPasswordConfirmText;
    private Button btnResetConfirm;
    private ProgressBar progressBar;
    private AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_new_password);

        //initialization
        resetPasswordPassText = findViewById(R.id.resetPassPassword);
        resetPasswordConfirmText = findViewById(R.id.resetPassConfirmPass);
        btnResetConfirm = findViewById(R.id.btnResetPassword);
        progressBar = findViewById(R.id.resetPasswordProgressBar);

        //initial setup
        progressBar.setVisibility(View.GONE);
        btnResetConfirm.setEnabled(false);
        btnResetConfirm.setBackgroundColor(getResources().getColor(R.color.graycolor));

        //validation setup
        awesomeValidation.addValidation(this,R.id.resetPassPassword,SignupActivity.regexPassword,R.string.err_password);
        awesomeValidation.addValidation(this,R.id.resetPassConfirmPass,R.id.resetPassPassword,R.string.err_confirmpassword);

        //listeners
        resetPasswordConfirmText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                awesomeValidation.validate();
                if(TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(resetPasswordPassText.getText().toString()))
                {
                    btnResetConfirm.setEnabled(false);
                    btnResetConfirm.setBackgroundColor(getResources().getColor(R.color.graycolor));
                }
                else
                {
                    btnResetConfirm.setEnabled(true);
                    btnResetConfirm.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });

        btnResetConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(awesomeValidation.validate())
                {
                    if(InternetCheckHelper.isInternetConnected(ForgotPasswordNewPasswordActivity.this))
                    {
                        String password = resetPasswordPassText.getText().toString();
                        String passwordConfirm = resetPasswordConfirmText.getText().toString();
                        String url = getString(R.string.urlResetPassword);
                        String email = getIntent().getStringExtra(ForgotPasswordActivity.EXTRA_FORGOT_EMAIL);
                        new UpdatePasswordTask(ForgotPasswordNewPasswordActivity.this)
                                .execute(url,email,password,passwordConfirm);
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    public void handleResponse(String response) {
        progressBar.setVisibility(View.GONE);
        HashMap<String,String> mapResponses = new HashMap<>(4);
        mapResponses.put(getString(R.string.err_email_notFound),getString(R.string.err_email_notFound_dialogMsg));
        mapResponses.put(getString(R.string.err_general),getString(R.string.err_general_dialogMsg));
        mapResponses.put(getString(R.string.err_server_nonresponsive),getString(R.string.err_server_nonresponsive_dialogMsg));
        mapResponses.put(getString(R.string.err_server),getString(R.string.err_server_dialogMsg));
        mapResponses.put(getString(R.string.err_database),getString(R.string.dialog_database_error));
        if(response.equals("success"))
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else
        {
            try
            {
                DialogFragmentHelper.showDialogFragment(this,mapResponses.get(response));
            }
            catch(Exception e)
            {
                e.printStackTrace();
                DialogFragmentHelper.showDialogFragment(this,R.string.err_general_dialogMsg);
            }

        }

    }
}
class UpdatePasswordTask extends AsyncTask<String,Void,String>{
    private WeakReference<ForgotPasswordNewPasswordActivity> forgotPasswordNewPasswordActivityWeakReference;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String email = strings[1];
        String password = strings[2];
        String passwordConfirm = strings[3];
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String response;
        JSONObject jsonObject = new JSONObject();
        try
        {
            String encryptedPass = AESCrypt.encrypt(password);
            encryptedPass = encryptedPass.substring(0,encryptedPass.length()-1);
            jsonObject.put("email",email);
            jsonObject.put("password",encryptedPass);
            RequestBody body = RequestBody.create(JSON,jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().string();
        }
        catch(IOException e)
        {
            response = "server nonresponsive";
            return response;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            response = "error";
            return response;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        forgotPasswordNewPasswordActivityWeakReference.get().handleResponse(response);
    }

    UpdatePasswordTask(ForgotPasswordNewPasswordActivity context)
    {
        forgotPasswordNewPasswordActivityWeakReference = new WeakReference<ForgotPasswordNewPasswordActivity>(context);
    }

}
