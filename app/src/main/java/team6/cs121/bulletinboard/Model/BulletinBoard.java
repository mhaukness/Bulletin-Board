package team6.cs121.bulletinboard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team6.cs121.bulletinboard.DataDownload.DataDownloadService;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard implements Parcelable {

    private String name;
    private List<Note> notes;
    public static final String NOTE_KEY = "notes";
    public static final String BOARD_NAME = "boardName";



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
        this.notes.add(0, note);
    }


    /**
     *
     * @param index
     * @return
     */
    public Note getNote(int index) {
        return this.notes.get(index);
    }


    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }


    /**
     *
     * @return
     */
    public List<Note> getAllNotes() {
        return this.notes;
    }


    public int numNotes() {
        return this.notes.size();
    }


    /**
     *
     * @param index
     */
    public void removeNote(int index) {
        this.notes.remove(index);
    }



    public static BulletinBoard createFromJSON(JSONObject jsonBoard) throws JSONException {
        BulletinBoard board = new BulletinBoard(jsonBoard.getString(BOARD_NAME));
        JSONArray noteArray = jsonBoard.getJSONArray(NOTE_KEY);
        for (int i = 0; i < noteArray.length(); ++i) {
            Note note = Note.createFromJSON(noteArray.getJSONObject(i));
            board.addNote(note);
        }
        return board;
    }


    public static JSONObject writeToJSON(BulletinBoard currentBoard) throws JSONException {
        JSONObject personalBoard = new JSONObject();
        List<Note> notes = currentBoard.getAllNotes();
        JSONArray noteArray = new JSONArray();
        for (int i = 0; i < notes.size(); ++i) {
            JSONObject jsonNote = Note.writeToJSON(notes.get(i));
            noteArray.put(i, jsonNote);
        }
        personalBoard.put(NOTE_KEY, noteArray);
        personalBoard.put(BOARD_NAME, currentBoard.getName());
        return personalBoard;
    }


    public static BulletinBoard createFromParse(ParseObject parseBoard) throws JSONException, ParseException {
        String name = "";
        if (parseBoard.containsKey(BOARD_NAME)) {
            name = parseBoard.getString(BOARD_NAME);
        }
        BulletinBoard board = new BulletinBoard(name);
        if (parseBoard.containsKey(DataDownloadService.PARSE_NOTES)) {
            List<ParseObject> currNotes = parseBoard.getList(DataDownloadService.PARSE_NOTES);
            for (int i = 0; i < currNotes.size(); ++i) {
                ParseObject currParseNote = currNotes.get(i);
                Note currNote = Note.createFromParse(currParseNote);
                board.addNote(currNote);
            }
        }
        return board;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.getAllNotes());
        dest.writeString(this.getName());
    }

    private BulletinBoard(Parcel in) {
        this.notes = new ArrayList<Note>();
        in.readList(this.notes, Note.class.getClassLoader());
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BulletinBoard> CREATOR = new Parcelable.Creator<BulletinBoard>() {

        @Override
        public BulletinBoard createFromParcel(Parcel source) {
            return new BulletinBoard(source);
        }

        @Override
        public BulletinBoard[] newArray(int size) {
            return new BulletinBoard[size];
        }
    };
}
