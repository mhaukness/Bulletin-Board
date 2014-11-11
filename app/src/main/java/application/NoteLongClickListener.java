package application;

import android.view.View;
import android.widget.PopupMenu;
import android.content.Context;

/**
 * Created by eirikhansen on 11/4/14.
 */
public class NoteLongClickListener implements View.OnLongClickListener {
    private NoteModifier activity;
    private Context cont;

    public NoteLongClickListener(NoteModifier act, Context cont){
        this.activity = act;
        this.cont = cont;
    }

    public boolean onLongClick(View v){
        Integer index = (Integer) v.getTag();
        PopupMenu popup = new PopupMenu(cont, v);
        popup.getMenuInflater().inflate(R.menu.notification_screen, popup.getMenu());
        popup.show();
        return true;
    }
}
