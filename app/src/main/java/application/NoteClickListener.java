package application;

import android.view.View;

/**
 * Created by alobb on 10/9/14.
 */
public class NoteClickListener implements View.OnClickListener {

    private NoteModifier editActivity;
    private ConfirmDelete.DeleteDialogListener deleteActivity;

    public NoteClickListener(NoteModifier activity1, ConfirmDelete.DeleteDialogListener activity2) {
        this.editActivity = activity1;
        this.deleteActivity = activity2;
    }

    @Override
    public void onClick(View v) {
        Integer index = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.edit_button:
                editActivity.startEditNote(index);
                break;
            case R.id.delete_button:
                deleteActivity.showDeleteDialog(index);
                break;
        }
    }
}
