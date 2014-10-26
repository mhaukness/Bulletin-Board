package application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 10/15/14.
 */
public class App extends Application {

    private static final String APPLICATION_ID = "zLVIHD2pn243N9DhZFqDGXQrYRtqpjOqUCq1nKqq";
    private static final String CLIENT_KEY = "IrVmsoQqhycibo4TNGGG36vZ8k9rrorWoaZpsdCU";


    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(BulletinBoard.class);
        ParseObject.registerSubclass(Note.class);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }


    public static String getApplicationId() {
        return APPLICATION_ID;
    }


    public static String getClientKey() {
        return CLIENT_KEY;
    }
}
