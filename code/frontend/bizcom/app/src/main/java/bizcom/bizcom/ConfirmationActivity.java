package bizcom.bizcom;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmationActivity extends AppCompatActivity{
    Button btnConfirm;
    EditText confirmationEditText;
    String phone;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        phone = getIntent().getStringExtra(SignupActivity.EXTRA_PHONE);
        sendConfirmationSMS();
        confirmationEditText = findViewById(R.id.confirmationCode);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);
        btnConfirm.setBackgroundColor(ConfirmationActivity.this.getResources().getColor(R.color.graycolor));




        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetCheckHelper.isInternetConnected(ConfirmationActivity.this))
                {
                    DialogFragmentHelper.showDialogFragment(ConfirmationActivity.this,R.string.dialog_internet_unavailable);
                }
                else
                {

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

        new ConfirmBackgroundTask(this).execute(url,phone);
    }

    public void handleConfirmationResponse(String response)
    {
        if(response=="")
        {
            DialogFragment newFragment = BizcomDialogFragment.newInstance(R.string.dialog_server_error);

            newFragment.show(this.getFragmentManager(),"dialog ".concat(this.getString(R.string.dialog_server_error)));
            newFragment.getFragmentManager().executePendingTransactions();
            newFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ConfirmationActivity.this.recreate();
                }
            });



        }
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
        String phone = strings[1];
        String response=null;
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("phone",phone);
            RequestBody body = RequestBody.create(JSON,jsonObject.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response res = client.newCall(request).execute();
            response = res.body().toString();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
            response = "";
        }
        catch(IOException e) //means that the call wasn't responded
        {
            response = "";

        }
        return response;

    }
    @Override
    protected void onPostExecute(String response) {
            confirmationActivityWeakReference.get().handleConfirmationResponse(response);
    }

}