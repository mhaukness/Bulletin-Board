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
 */
public class EditFragment extends Fragment {
    private Button saveButton;
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
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_note, container, false);
        this.saveButton = (Button) view.findViewById(R.id.save_edit_button);
        this.noteText = (EditText) view.findViewById(R.id.note_edit_text);
        this.saveButton.setOnClickListener(buttonClickListener);
        return view;
    }


    /**
     *
     * @param activity
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
     *
     * @param newText
     */
    public void setNoteText(String newText) {
        this.noteText.setText(newText);
    }


    public String getNoteText() {
        return this.noteText.getText().toString();
    }


    public void saveEdit() {
        activity.fragmentFinished(this);
    }
}
