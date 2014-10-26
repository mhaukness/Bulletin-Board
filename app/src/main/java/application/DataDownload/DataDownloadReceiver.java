package application.DataDownload;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by alobb on 10/24/14.
 */
public class DataDownloadReceiver extends ResultReceiver {
    private Receiver mReceiver;
    public static final String RECEIVER = "RECEIVER";


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


    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }


    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
