package bizcom.bizcom;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by Satan on 7/15/2018.
 */

public class InternetCheck extends AsyncTask<Void,Void,Boolean> {

    public interface AsyncResponse {void accept(Boolean internet);}
    public AsyncResponse delegate = null;


    @Override
    protected Boolean doInBackground(Void... voids) {
        try
        {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("8.8.8.8",53),1500);
            socket.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean internet)
    {
        delegate.accept(internet);
    }
}
