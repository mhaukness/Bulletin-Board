package application;

import android.view.View;

/**
 * Created by alobb on 10/9/14.
 */
public class NoteClickListener implements View.OnClickListener {
    private NoteModifier activity;


    public NoteClickListener(NoteModifier activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Integer index = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.edit_button:
                activity.startEditNote(index);
                break;
            case R.id.delete_button:
                activity.removeNote(index);
                break;
        }
    }
}
