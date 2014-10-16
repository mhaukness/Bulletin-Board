package team6.cs121.bulletinboard;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alobb on 9/28/14.
 */
public class Note {

    private String text;
    private static final String NOTE_VALUE = "text";


    /**e
     * Create a new Note
     * @param
     */
    public Note() {
        this.text = "";
    }


    /**
     *
     * @param text
     */
    public Note(String text) {
        this.text = text;
    }


    public String getText() {
        return this.text;
    }


    /**
     *
     * @param newText
     */
    public void editText(String newText) {
        this.text = newText;
    }


    public static Note createFromJSON(JSONObject noteObject) throws JSONException {
        Note note = new Note(noteObject.getString(NOTE_VALUE));
        return note;
    }

    public static JSONObject writeToJSON(Note note) throws JSONException {
        JSONObject jsonNote = new JSONObject();
        jsonNote.put(NOTE_VALUE, note.getText());
        return jsonNote;
    }
}
