package application.DataDownload;

import android.os.Bundle;

/**
 * Created by alobb on 10/26/14.
 * This interface should be implemented by any activities that start
 *  {@link application.DataDownload.DataDownloadService}.
 */
public interface DataReceiver {

    /**
     * This function is called when the service has completed.  It is a callback to the activity
     *  that started the service.
     * @param resultCode The code sent by {@link application.DataDownload.DataDownloadService}
     * @param resultData The bundle sent by {@link application.DataDownload.DataDownloadService}
     */
    public void onReceiveResult(int resultCode, Bundle resultData);

}
