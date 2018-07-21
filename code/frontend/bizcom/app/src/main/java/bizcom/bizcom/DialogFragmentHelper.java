package bizcom.bizcom;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Created by Satan on 7/21/2018.
 */

public class DialogFragmentHelper {
    public static void showDialogFragment(Activity context,int resourceID)
    {
        DialogFragment newFragment = BizcomDialogFragment.newInstance(resourceID);
        newFragment.show(context.getFragmentManager(),"dialog ".concat(context.getString(resourceID)));
    }
    public static void showDialogFragment(Activity context,String message)
    {
        DialogFragment newFragment = BizcomDialogFragment.newInstance(message);
        newFragment.show(context.getFragmentManager(),"dialog ".concat(message));
    }
}
