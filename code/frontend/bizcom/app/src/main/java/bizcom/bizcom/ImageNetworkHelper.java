package bizcom.bizcom;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Satan on 8/1/2018.
 */

public class ImageNetworkHelper {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CLEAR_APP_CACHE
    };
    public static boolean requestPermissions(Activity context)
    {
        int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission3 = ActivityCompat.checkSelfPermission(context,Manifest.permission.CLEAR_APP_CACHE);
        if (permission1 != PackageManager.PERMISSION_GRANTED || permission2!=PackageManager.PERMISSION_GRANTED
                || permission3!=PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    context,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permission2 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return !(permission1 != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED);
    }


    public static void getProfilePhoto(Activity context,String userName,int imageViewResID)
    {
        Uri.Builder builder = new Uri.Builder();


            builder.scheme("http")
                    .encodedAuthority(context.getString(R.string.urlBase))
                    .appendPath("profile")
                    .appendPath("getProfilePic")
                    .appendQueryParameter("userName",userName);




        ImageNetworkHelper.downloadImage(context,builder.build().toString(),imageViewResID,R.drawable.emptyimage,R.drawable.emptyimage);
    }
    public static void selectImage(Activity context,int request){

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        context.startActivityForResult(chooserIntent, request);

    }
    public static String getPathFromURI(Activity context,Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context.getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void uploadImage(Activity context,String path,String url,String headerName,String headerValue)
    {
        File f = new File(path);

        Future uploading = Ion.with(context)
                .load("POST",url).addHeader(headerName,headerValue)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        System.out.println(uploaded/total*100);
                    }
                })
                .setMultipartFile("image","image/jpeg", f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        try {
                            JSONObject jobj = new JSONObject(result.getResult());


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });
    }
    public static void uploadImage(Activity context,String path,String url,String headerName,String headerValue,String headerName1,String headerValue1)
    {
        File f = new File(path);

        Future uploading = Ion.with(context)
                .load("POST",url).addHeader(headerName,headerValue)
                .addHeader(headerName1,headerValue1)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long uploaded, long total) {
                        System.out.println(uploaded/total*100);
                    }
                })
                .setMultipartFile("image","image/jpeg", f)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        try {
                            JSONObject jobj = new JSONObject(result.getResult());


                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });
    }
    public static void downloadImage(Activity context,String url,int ImageViewResourceID,int placeHolderResourceID,int errorResourceID)
    {
        System.out.println(url);
        ImageView imageView = context.findViewById(ImageViewResourceID);
        Ion.with(imageView)
                .placeholder(placeHolderResourceID)
                .error(errorResourceID)
                .load(url);
    }


    public static void deleteImage(final ProfileActivity context, final String path, String url, String userNameHeading, String userName, String numberHeading, String number,final int requestNo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(userNameHeading,userName);
            jsonObject.put(numberHeading,number);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    context.hasBeenDeleted(requestNo,path);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getMinstance(context).addToRequestQueue(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
