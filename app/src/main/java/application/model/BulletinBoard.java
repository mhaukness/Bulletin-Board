package application.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alobb on 9/28/14.
 */
@ParseClassName("Board")
public class BulletinBoard extends ParseObject {

    public static final String NOTE_KEY = "notes";
    public static final String BOARD_NAME = "boardName";


    public BulletinBoard() {
        // Required for parseObject
    }


    /**
     * Create a new Bulletin Board
     * @param name
     */
    public BulletinBoard(String name) {
        super();
        this.put(BOARD_NAME, name);
    }


    public String getId() {
        return this.getObjectId();
    }


    public Date getLastUpdate() {
        return this.getUpdatedAt();
    }


    /**
     *
     * @param note
     */
    public void addNote(Note note) throws JSONException {
        JSONArray jsonNotes = this.getJSONArray(NOTE_KEY);
        jsonNotes.put(0, note);
        this.put(NOTE_KEY, jsonNotes);
    }


    /**
     *
     * @param index
     * @return
     */
    public Note getNote(int index) {
        try {
            return (Note) this.getJSONArray(NOTE_KEY).get(index);
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    /**
     *
     * @return
     */
    public String getName() {
        return this.getString(BOARD_NAME);
    }


    /**
     *
     * @return
     */
    public List<Note> getAllNotes() {
        if (this.getJSONArray(NOTE_KEY) == null) {
            new ArrayList<Note>();
        }
        JSONArray jsonNotes = this.getJSONArray(NOTE_KEY);
        List<Note> notes = new ArrayList<Note>();
        for (int i = 0; i < jsonNotes.length(); ++i) {
            try {
                notes.add((Note) jsonNotes.get(i));
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        return notes;
    }


    public int numNotes() {
        if (this.getJSONArray(NOTE_KEY) == null) {
            return 0;
        }
        return this.getJSONArray(NOTE_KEY).length();
    }


    /**
     *
     * @param index
     */
    public void removeNote(int index) throws JSONException {
        JSONArray jsonNotes = this.getJSONArray(NOTE_KEY);
        JSONArray newJsonNotes = new JSONArray();
        for (int i = 0; i < jsonNotes.length(); ++i) {
            if (i != index) {
                newJsonNotes.put(jsonNotes.get(i));
            }
        }
        this.put(NOTE_KEY, newJsonNotes);
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
}
