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

public class InternetUnavaliableDialogFragment extends DialogFragment {
    public interface InternetUnavailableListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    InternetUnavailableListener listener;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (InternetUnavailableListener) activity;

        } catch(ClassCastException e)
        {
            //the activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()+" must Implement InternetUnavailableListener");

        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_internet_unavailable)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveClick(InternetUnavaliableDialogFragment.this); //redirect the event to the parent
                    }
                });
        return builder.create();
    }
}
