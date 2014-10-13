package team6.cs121.bulletinboard;

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
            case R.id.delete_button:
                activity.removeNote(index);
                break;
            case R.id.note_textview:
                activity.editNote(index);
                break;
        }
    }
}
