package team6.cs121.bulletinboard;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by alobb on 10/15/14.
 */
public class App extends Application {

    private static final String APPLICATION_ID = "zLVIHD2pn243N9DhZFqDGXQrYRtqpjOqUCq1nKqq";
    private static final String CLIENT_KEY = "IrVmsoQqhycibo4TNGGG36vZ8k9rrorWoaZpsdCU";


    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }


    public void test() {

    }
}
