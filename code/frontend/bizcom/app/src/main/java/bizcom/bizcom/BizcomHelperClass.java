package bizcom.bizcom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by Satan on 8/4/2018.
 */

class BizcomHelperClass {
    public static void logout(final Activity context, JSONObject user) {
        JSONObject jsonObject = new JSONObject();
        try
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .encodedAuthority(context.getString(R.string.urlBase))
                    .appendPath("userAccounts")
                    .appendPath("logout");
            jsonObject.put("userName",user.getString("userName"));
            JsonObjectRequest newRequest = new JsonObjectRequest(Request.Method.POST, builder.build().toString(),
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                        System.out.println(response);
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                }
            });
            MySingleton.getMinstance(context).addToRequestQueue(newRequest);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
