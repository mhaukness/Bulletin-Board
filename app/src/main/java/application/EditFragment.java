package application;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by alobb on 10/26/14.
 * This class contains the definition of the fragment that is used for editing the text of notes.
 */
public class EditFragment extends Fragment {
    private EditText noteText;
    private FragmentCallback activity;
    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_edit_button:
                    saveEdit();
            }
        }
    };


    /**
     * This function is called when the Fragment is first created
     * @param inflater The inflater to create the layout with
     * @param container The container of the fragment
     * @param savedInstanceState The saved state of this fragment
     * @return The view that is inflated by this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_note, container, false);

        Button saveButton = (Button) view.findViewById(R.id.save_edit_button);
        this.noteText = (EditText) view.findViewById(R.id.note_edit_text);
        saveButton.setOnClickListener(buttonClickListener);
        return view;
    }


    /**
     * Saves a reference to the activity so that we can use a callback later
     * @param activity The activity that created this fragment
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentCallback) {
            this.activity = (FragmentCallback) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement FragmentCallback");
        }
    }


    /**
     * Set the text to the current value of the note
     * @param currText The current text of the note
     */
    public void setNoteText(String currText) {
        this.noteText.setText(currText);
    }


    /**
     * @return The text of the note
     */
    public String getNoteText() {
        return this.noteText.getText().toString();
    }


    /**
     * This function is called when the user is done with editing the note and is ready to save it.
     */
    public void saveEdit() {
        activity.fragmentFinished(this);
    }
}
