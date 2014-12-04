package application;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.content.Context;
import android.widget.TextView;
import android.content.Intent;
import java.util.GregorianCalendar;

/**
 * Created by eirikhansen on 11/4/14.
 */
public class NoteLongClickListener implements View.OnLongClickListener {
    private NoteModifier activity;
    private Context cont;
    private TextView txt;
    private static String current;

    public NoteLongClickListener(NoteModifier act, Context cont, TextView txt){
        this.activity = act;
        this.cont = cont;
        this.txt = txt;
    }

    public boolean onLongClick(View v){
        this.current = txt.getText().toString();
        Integer index = (Integer) v.getTag();
        PopupMenu popup = new PopupMenu(cont, v);
        popup.getMenuInflater().inflate(R.menu.notification_screen, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Long time = new GregorianCalendar().getTimeInMillis();
                switch(menuItem.getItemId()){
                    case R.id.one_minute:
                        time = time+(60*1000);
                        break;
                    case R.id.five_minutes:
                        time = time+(5*60*1000);
                        break;
                    case R.id.half_hour:
                        time = time+(30*60*1000);
                        break;
                    case R.id.hour:
                        time = time+(60*60*1000);
                        break;
                    case R.id.two_hours:
                        time = time+(2*60*60*1000);
                        break;
                    case R.id.six_hours:
                        time = time+(6*60*60*1000);
                        break;
                    case R.id.twelve_hours:
                        time = time+(12*60*60*1000);
                        break;
                    case R.id.one_day:
                        time = time+(24*60*60*1000);
                        break;
                    case R.id.two_days:
                        time = time+(2*24*60*60*1000);
                        break;
                    case R.id.five_days:
                        time = time+(5*24*60*60*1000);
                }
                Intent alarm = new Intent(cont, AlarmReceiver.class);

                AlarmManager malarm = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);

                malarm.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(cont, 1, alarm, PendingIntent.FLAG_UPDATE_CURRENT ) );
                return true;
            }
        });
        return true;
    }


    public static String getCurrent(){
        return current;
    }
}
