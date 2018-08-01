package bizcom.bizcom;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Satan on 8/1/2018.
 */

public class ImageNetworkHelper {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static boolean requestPermissions(Activity context)
    {
        int permission1 = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission1 != PackageManager.PERMISSION_GRANTED || permission2!=PackageManager.PERMISSION_GRANTED) {
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
    public static void downloadImage(Activity context,String url,int ImageViewResourceID,int placeHolderResourceID,int errorResourceID)
    {
        ImageView imageView = context.findViewById(ImageViewResourceID);
        Ion.with(imageView)
                .placeholder(placeHolderResourceID)
                .error(errorResourceID)
                .load(url);
    }
}
