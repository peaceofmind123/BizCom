package bizcom.bizcom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Satan on 7/19/2018.
 */

public class BizcomDialogFragment extends DialogFragment {
    private int resourceID;
    private String message;






    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

    }
    public static BizcomDialogFragment newInstance(int resourceID)
    {
        BizcomDialogFragment f = new BizcomDialogFragment();
        Bundle args = new Bundle();
        args.putInt("resourceID",resourceID);
        f.setArguments(args);
        return f;

    }
    public static BizcomDialogFragment newInstance(String message)
    {
        BizcomDialogFragment f = new BizcomDialogFragment();
        Bundle args = new Bundle();
        args.putString("message",message);
        f.setArguments(args);
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        this.resourceID = args.getInt("resourceID",R.string.NullResID); //the default parameter
        this.message = args.getString("message",getString(R.string.NullResID));
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(this.resourceID ==R.string.NullResID)
        {
            builder.setMessage(this.message);
        }
        else
            builder.setMessage(this.resourceID);


        return builder.create();
    }
}
