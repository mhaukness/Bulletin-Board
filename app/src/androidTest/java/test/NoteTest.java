package test;

import android.test.AndroidTestCase;

import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import application.App;
import application.DataDownload.ParseKeywords;
import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 10/24/14.
 */
public class NoteTest extends AndroidTestCase {
    private static final String OLD_TEXT = "noteText";
    private static final String NEW_TEXT = "newNoteText";
    private static final String ID = "123456";
    private static JSONObject JSON_NOTE;


    protected void setUp() throws Exception {
        ParseObject.registerSubclass(BulletinBoard.class);
        ParseObject.registerSubclass(Note.class);
        Parse.initialize(getContext(), App.getApplicationId(), App.getClientKey());
        JSON_NOTE = new JSONObject();
        JSON_NOTE.put(ParseKeywords.PARSE_NOTE_VALUE, OLD_TEXT);
    }


    public void testConstructor() {
        ParseObject.registerSubclass(BulletinBoard.class);

        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
    }


    public void testEdit() {
        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
        note.setText(NEW_TEXT);
        assertEquals(NEW_TEXT, note.getText());
    }


    public void testEditSame() {
        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
        note.setText(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
    }


    public void testWriteToJson() throws JSONException {
        Note note = new Note(OLD_TEXT);
        String noteToJson = Note.writeToJSON(note).toString().replaceAll(" ", "");
        assertEquals(JSON_NOTE.toString(), noteToJson);
    }


    public void testReadFromJson() throws JSONException {
        Note note = Note.createFromJSON(JSON_NOTE);
        assertEquals(OLD_TEXT, note.getText());
    }
}
