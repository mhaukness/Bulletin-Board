package team6.cs121.bulletinboard;

import android.os.Parcel;

import com.parse.ParseObject;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

import team6.cs121.bulletinboard.DataDownload.DataDownloadService;
import team6.cs121.bulletinboard.Model.Note;

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
        JSON_NOTE.put("text", OLD_TEXT);
    }


    public void testConstructor() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
        assertEquals(OLD_TEXT, note.getText());
        assertEquals(ID, note.getId());
    }


    public void testEdit() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
        assertEquals(OLD_TEXT, note.getText());
        note.editText(NEW_TEXT);
        assertEquals(NEW_TEXT, note.getText());
    }


    public void testEditSame() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
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


    public void testParcelable() {
        Note note = new Note(OLD_TEXT);
        Parcel parcel = Parcel.obtain();
        note.writeToParcel(parcel, 0);
        //done writing, now reset parcel for reading
        parcel.setDataPosition(0);
        //finish round trip
        Note createFromParcel = Note.CREATOR.createFromParcel(parcel);
        assertEquals(note, createFromParcel);
    }


    public void testCreateParse() {
        Note note = new Note(OLD_TEXT);
        ParseObject parseNote = Note.createParseNote(note);
        assertTrue(parseNote.containsKey(Note.NOTE_VALUE));
        assertEquals(parseNote.get(Note.NOTE_VALUE), OLD_TEXT);
    }


    public void testUpdateParse() {
        Note note = new Note(OLD_TEXT);
        ParseObject parseNote = new ParseObject(DataDownloadService.PARSE_NOTE_CLASS);
        parseNote.put(Note.NOTE_VALUE, NEW_TEXT);
        parseNote = Note.updateParse(parseNote, note);
        assertTrue(parseNote.containsKey(Note.NOTE_VALUE));
        assertEquals(parseNote.get(Note.NOTE_VALUE), OLD_TEXT);
    }
}
