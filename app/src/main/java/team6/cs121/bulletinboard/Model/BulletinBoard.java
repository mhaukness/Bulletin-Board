package team6.cs121.bulletinboard.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseException;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team6.cs121.bulletinboard.DataDownload.DataDownloadService;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard implements Parcelable {

    private String name;
    private List<Note> notes;
    public static final String NOTE_KEY = "notes";
    public static final String BOARD_NAME = "boardName";
    private String id;
    private Date lastUpdate;



    /**
     * Create a new Bulletin Board
     * @param name
     */
    public BulletinBoard(String name) {
        this.notes = new ArrayList<Note>();
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return this.id;
    }


    public void setLastUpdate(Date date) {
        this.lastUpdate = date;
    }


    public Date getLastUpdate() {
        return this.lastUpdate;
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
        board.setId(parseBoard.getObjectId());
        board.setLastUpdate(parseBoard.getUpdatedAt());
        return board;
    }


    public static ParseObject updateParse(ParseObject parseBoard, BulletinBoard board) throws JSONException {
        if (!board.getName().equals(parseBoard.get(BOARD_NAME))) {
            parseBoard.put(BOARD_NAME, board.getName());
        }
        Map<ParseObject, Note> noteMap = new HashMap<ParseObject, Note>();
        List<Note> notes = board.getAllNotes();
        int numNewNotes = 0;
        for (Note note : notes) {
            if (!note.getId().isEmpty()) {
                break;
            }
            ++numNewNotes;
        }
        JSONArray jsonNotes = parseBoard.getJSONArray(DataDownloadService.PARSE_NOTES);
        for (int i = numNewNotes; i < notes.size(); ++i) {
            ParseObject parseNote = Note.updateParse((ParseObject) jsonNotes.get(i), notes.get(i));
            jsonNotes.put(i, parseNote);
        }
        for (int i = 0; i < numNewNotes; ++i) {
            ParseObject parseNote = Note.createParseNote(notes.get(i));
            jsonNotes.put(i, parseNote);
        }
        parseBoard.put(DataDownloadService.PARSE_NOTES, jsonNotes);
        return parseBoard;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || (other.getClass() != ((Object) this).getClass())) {
            return false;
        }
        BulletinBoard otherBoard = (BulletinBoard) other;
        if (this.numNotes() != otherBoard.numNotes()) {
            return false;
        }
        for (int i = 0; i < this.numNotes(); ++i) {
            if (this.getNote(i).equals(otherBoard.getNote(i))) {
                return false;
            }
        }
        return this.getName().equals(otherBoard.getName()) &&
               this.getId().equals(otherBoard.getId()) &&
               (this.getLastUpdate().compareTo(otherBoard.getLastUpdate()) == 0);
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
        dest.writeList(this.getAllNotes());
        dest.writeString(this.getName());
        dest.writeString(this.getId());
        dest.writeValue(this.getLastUpdate());
    }


    private BulletinBoard(Parcel in) {
        this.notes = new ArrayList<Note>();
        in.readList(this.notes, Note.class.getClassLoader());
        this.name = in.readString();
        this.id = in.readString();
        this.lastUpdate = (Date) in.readValue(Date.class.getClassLoader());
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
