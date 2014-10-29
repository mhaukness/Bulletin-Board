package application;

import android.app.Fragment;

/**
 * Created by alobb on 10/26/14.
 * This interface should be implemented by any activities that use fragments so that the fragment
 *  can call methods when certain actions are taken.
 */
public interface FragmentCallback {

    /**
     * This function is called when the fragment is done with processing and is ready for the
     *  activity to take back control.
     * @param fragment The fragment that is finished
     */
    public void fragmentFinished(Fragment fragment);
}
