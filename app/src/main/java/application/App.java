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

    /**
     * The application ID used in Parse.com for the Bulletin Board project.
     */
    private static final String APPLICATION_ID = "zLVIHD2pn243N9DhZFqDGXQrYRtqpjOqUCq1nKqq";

    /**
     * The client key used by Android for Parse.com
     */
    private static final String CLIENT_KEY = "IrVmsoQqhycibo4TNGGG36vZ8k9rrorWoaZpsdCU";


    /**
     * Called when the application is created.  We need to register our {@link com.parse.ParseObject}
     *  subclasses as well as call
     *  {@link com.parse.Parse#initialize(android.content.Context, String, String)}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(BulletinBoard.class);
        ParseObject.registerSubclass(Note.class);
        Parse.initialize(this, getApplicationId(), getClientKey());
    }


    /**
     * @return The application ID used in Parse.com
     */
    public static String getApplicationId() {
        return APPLICATION_ID;
    }


    /**
     * @return The client key used in Parse.com
     */
    public static String getClientKey() {
        return CLIENT_KEY;
    }
}
