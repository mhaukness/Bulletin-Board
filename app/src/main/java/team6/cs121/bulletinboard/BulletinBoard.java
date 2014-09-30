package team6.cs121.bulletinboard;

import android.app.Activity;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import team6.cs121.bulletinboard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard extends Activity{

    private String name;
    private List<Note> notes;

    Button add_note_button;


    /**
     * Create a new Bulletin Board
     * @param name
     */
    public BulletinBoard(String name) {
        this.notes = new ArrayList<Note>();
        this.name = name;
    }


    /**
     *
     * @param note
     */
    public void addNote(Note note) {
        this.notes.add(note);
    }


    /**
     *
     * @param index
     * @return
     */
    public Note getNote(int index) {
        return this.notes.get(index);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.add_note_button) {
            EditText newText = (EditText) findViewById(R.id.note_creation_area);
            Editable text = newText.getText();
            Note newNote = new Note(text.toString());
            this.notes.add(newNote);
            newText.setText("");

        }


    }

}
