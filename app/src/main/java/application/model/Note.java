package application.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import application.DataDownload.DataDownloadService;

/**
 * Created by alobb on 9/28/14.
 */
@ParseClassName("Note")
public class Note extends ParseObject {


    /**e
     * Create a new Note
     */
    public Note() {
        // Required when extending ParseObject
    }


    /**
     *
     * @param text
     */
    public Note(String text) {
        this.put(DataDownloadService.PARSE_NOTE_VALUE, text);
    }


    public String getText() {
        return this.getString(DataDownloadService.PARSE_NOTE_VALUE);
    }


    /**
     *
     * @return
     */
    public String getId() {
        return this.getObjectId();
    }


    /**
     *
     * @param newText
     */
    public void editText(String newText) {
        this.put(DataDownloadService.PARSE_NOTE_VALUE, newText);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || (other.getClass() != ((Object) this).getClass())) {
            return false;
        }
        Note otherNote = (Note) other;
        return this.getText().equals(otherNote.getText()) && this.getId().equals(otherNote.getId());
    }


    public static Note createFromJSON(JSONObject noteObject) throws JSONException {
        Note note = new Note(noteObject.getString(DataDownloadService.PARSE_NOTE_VALUE));
        return note;
    }

    public static JSONObject writeToJSON(Note note) throws JSONException {
        JSONObject jsonNote = new JSONObject();
        jsonNote.put(DataDownloadService.PARSE_NOTE_VALUE, note.getText());
        return jsonNote;
    }
}
