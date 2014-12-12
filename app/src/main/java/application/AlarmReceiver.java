package application;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.Notification.Builder;


/**
 * Created by eirikhansen on 11/25/14.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static String current;
    public AlarmReceiver(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent resultIntent = new Intent(context, App.class);
        PendingIntent result = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notif = new Notification.Builder(context)
                .setContentTitle("BulletinBoard Reminder!")
                .setContentText(NoteLongClickListener.getCurrent())
                .setSmallIcon(R.drawable.logo_3)
                .setContentIntent(result)
                .build();
        NotificationManager mannotif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mannotif.notify(1, notif);

    }
}
