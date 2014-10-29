package application.DataDownload;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by alobb on 10/24/14.
 * This class is used by {@link application.DataDownload.DataDownloadService} to call a method in
 *  the current activity that the service has finished saving or loading the boards.
 */
public class DataDownloadReceiver extends ResultReceiver {
    private DataReceiver mReceiver;
    public static final String RECEIVER_FLAG = "receiver";


    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public DataDownloadReceiver(Handler handler) {
        super(handler);
    }


    /**
     * Sets the receiver for our custom receiver
     * @param receiver The new receiver
     */
    public void setReceiver(DataReceiver receiver) {
        mReceiver = receiver;
    }


    /**
     *
     * @param resultCode
     * @param resultData
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
