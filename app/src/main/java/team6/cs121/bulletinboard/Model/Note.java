package team6.cs121.bulletinboard.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import team6.cs121.bulletinboard.DataDownload.DataDownloadService;

/**
 * Created by alobb on 9/28/14.
 */
@ParseClassName("Note")
public class Note extends ParseObject implements Parcelable {


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

    public static Note createFromParse(ParseObject currParseNote) {
        Note note = new Note();
        if (currParseNote.containsKey(DataDownloadService.PARSE_NOTE_VALUE)) {
            note.editText(currParseNote.getString(DataDownloadService.PARSE_NOTE_VALUE));
        }
        return note;
    }


    public static ParseObject createParseNote(Note note) {
        ParseObject parseNote = new ParseObject(DataDownloadService.PARSE_NOTE_CLASS);
        return Note.updateParse(parseNote, note);
    }


    public static ParseObject updateParse(ParseObject parseNote, Note note) {
        parseNote.put(DataDownloadService.PARSE_NOTE_VALUE, note.getText());
        return parseNote;
    }


    ////////////////////////////////////
    // Functions for Parcelable
    ////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getText());
        dest.writeString(this.getId());
    }


    private Note(Parcel in) {
        this.text = in.readString();
        this.id = in.readString();
    }


    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }


        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
