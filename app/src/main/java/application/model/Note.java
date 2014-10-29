package application.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import application.DataDownload.ParseKeywords;

/**
 * Created by alobb on 9/28/14.
 * This class contains the definition of a note object; it contains the text contained in the note.
 * It extends ParseObject so that it is easy to save and load the data from Parse.com.
 */
@ParseClassName(ParseKeywords.NOTE_CLASS)
public class Note extends ParseObject {

    /**
     * A boolean that corresponds to if the {@link application.model.Note Note} is currently being
     *  edited in the application.
     */
    private boolean isBeingEdited = false;


    /**
     *  Required for ParseObject subclass; nothing needs to happen here.
     */
    public Note() {
        // Required when extending ParseObject
    }


    /**
     * Create a new Note with the given text.  This does not save the Note to the device or Parse.com;
     *  this must be handled by the board that contains this note.
     * @param text The text of this note
     */
    public Note(String text) {
        this.put(ParseKeywords.PARSE_NOTE_VALUE, text);
    }


    /**
     * @return The text on this board
     */
    public String getText() {
        return this.getString(ParseKeywords.PARSE_NOTE_VALUE);
    }


    /**
     * @return {@link com.parse.ParseObject#getObjectId()}
     */
    public String getId() {
        return this.getObjectId();
    }


    /**
     *
     * @param newText
     */
    public void setText(String newText) {
        if (!this.getText().equals(newText)) {
            this.put(ParseKeywords.PARSE_NOTE_VALUE, newText);
        }
    }


    /**
     * @return Whether or not this note is being edited
     */
    public boolean isBeingEdited() {
        return this.isBeingEdited;
    }


    /**
     * Sets the edit status of this note.
     * @param newEditStatus Whether or not the note is currently being edited
     */
    public void setBeingEdited(boolean newEditStatus) {
        this.isBeingEdited = newEditStatus;
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


    /**
     *
     * @param noteObject
     * @return
     * @throws JSONException
     */
    public static Note createFromJSON(JSONObject noteObject) throws JSONException {
        Note note = new Note(noteObject.getString(ParseKeywords.PARSE_NOTE_VALUE));
        return note;
    }


    /**
     *
     * @param note
     * @return
     * @throws JSONException
     */
    public static JSONObject writeToJSON(Note note) throws JSONException {
        JSONObject jsonNote = new JSONObject();
        jsonNote.put(ParseKeywords.PARSE_NOTE_VALUE, note.getText());
        return jsonNote;
    }


    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Text: ");
        sb.append(this.getText());
        return sb.toString();
    }
}
