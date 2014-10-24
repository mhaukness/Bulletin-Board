package team6.cs121.bulletinboard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import team6.cs121.bulletinboard.DataDownload.DataDownloadService;

/**
 * Created by alobb on 9/28/14.
 */
public class Note implements Parcelable {

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

    public static Note createFromParse(ParseObject currParseNote) {
        Note note = new Note();
        if (currParseNote.containsKey(DataDownloadService.PARSE_NOTE_VALUE)) {
            note.editText(currParseNote.getString(DataDownloadService.PARSE_NOTE_VALUE));
        }
        return note;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getText());
    }

    private Note(Parcel in) {
        this.text = in.readString();
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
