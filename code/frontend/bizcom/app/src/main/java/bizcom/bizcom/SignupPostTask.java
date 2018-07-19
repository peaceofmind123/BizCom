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
        try
        {
            System.out.println(response);
            if(response.equals("success"))
            {
                /* todo: after merging the login branch, uncomment this code to redirect to login
                //create intent to redirect to login page
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra(SignupActivity.EXTRA_USER,json); //the json object is passed as a string, which will be parsed on the other side
                signupActivityWeakReference.get().startActivity(intent);
                */
            }
            else
            {
                // todo: give an error to the user
            }
        }
        catch(NullPointerException e) //happens when there is a network error which results in a null response
        {
            e.printStackTrace(); //todo: send some response to the user
        }
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

