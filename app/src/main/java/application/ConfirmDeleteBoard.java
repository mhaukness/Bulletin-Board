package application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Marina on 11/10/2014.
 *
 * Followed tutorial found at http://developer.android.com/guide/topics/ui/dialogs.html
 */
public class ConfirmDeleteBoard extends DialogFragment {

    private int noteIndex;

    /* An activity that uses this dialog fragment must implement this interface
        in order to receive callbacks.
        The methods pass the DialogFragment in case the host needs to query it.
    */
    public interface DeleteDialogListener {
        public void showDeleteDialog(int index);
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // making an instance of the interface to deliver action events
    DeleteDialogListener mListener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DeleteDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // make a new AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set characteristics of the dialog
        builder.setMessage("Are you sure you want to delete this?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ConfirmDeleteBoard.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ConfirmDeleteBoard.this);
                    }
                });

        // get the AlertDialog from create()
        return builder.create();

    }


}
