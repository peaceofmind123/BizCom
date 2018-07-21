package bizcom.bizcom;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import bizcom.bizcom.SignupActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupPostTask extends AsyncTask<String,Void,String> {


    //the okhttp singleton
    OkHttpClient client=new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Exception exception;
    private String response;
    private String json;
    //the weak reference object to prevent memory leaks during garbage collection of signupActivity object
    private WeakReference<SignupActivity> signupActivityWeakReference;

    SignupPostTask(SignupActivity context)
    {
        signupActivityWeakReference = new WeakReference<>(context);
    }
    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        json = params[1];

        response = null;
        try {
            response = doPostRequest(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;


    }

    @Override
    protected void onPostExecute(String response) {

        signupActivityWeakReference.get().handlePostResponse(response);
    }

    private String doPostRequest(String url, String json) throws IOException,NullPointerException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

