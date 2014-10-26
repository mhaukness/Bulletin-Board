package test;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import application.DataDownload.DataDownloadService;
import application.model.Note;

/**
 * Created by alobb on 10/24/14.
 */
public class NoteTest extends TestCase {
    private static final String OLD_TEXT = "noteText";
    private static final String NEW_TEXT = "newNoteText";
    private static final String ID = "123456";
    private static JSONObject JSON_NOTE;


    protected void setUp() throws Exception {
        JSON_NOTE = new JSONObject();
        JSON_NOTE.put(DataDownloadService.PARSE_NOTE_VALUE, OLD_TEXT);
    }


    public void testConstructor() {
        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
    }


    public void testEdit() {
        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
        note.editText(NEW_TEXT);
        assertEquals(NEW_TEXT, note.getText());
    }


    public void testEditSame() {
        Note note = new Note(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
        note.editText(OLD_TEXT);
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
