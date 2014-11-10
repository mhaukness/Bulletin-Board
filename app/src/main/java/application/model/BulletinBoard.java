package application.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.DataDownload.ParseKeywords;

/**
 * Created by alobb on 9/28/14.
 * This class contains the definition of a board object; it contains the list of notes that show up
 *  on the board.  It extends ParseObject so that it is easy to save and load the data from Parse.com.
 */
@ParseClassName(ParseKeywords.BOARD_CLASS)
public class BulletinBoard extends ParseObject {
    private boolean isPersonalBoard;


    /**
     * Required for ParseObject subclass; nothing needs to happen here.
     */
    public BulletinBoard() {}


    /**
     * Creates a new Bulletin Board.  Note that this does not save the board to Parse or the device;
     *  that needs to be handled by the activity that creates the board.
     * @param name The name of the new bulletin board
     */
    public BulletinBoard(String name) {
        super();
        this.put(ParseKeywords.BOARD_NAME, name);
        this.put(ParseKeywords.BOARD_NOTE_ARRAY, new ArrayList<Note>());
    }


    public static BulletinBoard createPersonalBoard(String name) {
        BulletinBoard board = new BulletinBoard(name);
        board.setPersonalBoard(true);
        return board;
    }


    public static BulletinBoard createGroupBoard(String name) {
        BulletinBoard board = new BulletinBoard(name);
        ParseUser currUser = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = board.getRelation(ParseKeywords.MODERATORS);
        relation.add(currUser);
        try {
            board.save();
        } catch (ParseException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        return board;
    }


    /**
     * Add a note to the board at the beginning of the board (The beginning is the most chronologically
     *  recent).
     * @param note
     */
    public void addNote(Note note) {
        List<Note> notes = this.getAllNotes();
        notes.add(0, note);
        this.put(ParseKeywords.BOARD_NOTE_ARRAY, notes);
    }


    /**
     * @param index The index of the note to get
     * @return A {@link application.model.Note Note}
     */
    public Note getNote(int index) {
        return this.getNoteArray().get(index);
    }


    /**
     * Get the Parse user object that created this Board.
     * Note that this function returns null if the board is a personal board that is not saved to
     *  Parse.com
     * @return The creator of the board
     */
    public List<ParseUser> getModerators() {
        if (this.isPersonalBoard) {
            return new ArrayList<ParseUser>();
        }
        ParseRelation<ParseUser> moderatorRelation = this.getRelation(ParseKeywords.MODERATORS);
        ParseQuery query = moderatorRelation.getQuery();
        try {
            return query.find();
        } catch (ParseException e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    /**
     * @return The name of the board
     */
    public String getBoardName() {
        return this.getString(ParseKeywords.BOARD_NAME);
    }


    /**
     *
     * @return A list of the notes in this board
     */
    public List<Note> getAllNotes() {
        return this.getNoteArray();
    }


    /**
     *
     * @return The number of notes in this board
     */
    public int numNotes() {
        if (this.getNoteArray() == null) {
            return 0;
        }
        return this.getNoteArray().size();
    }


    /**
     * Remove a note at the given index
     * @param index The index fo the note to remove
     */
    public void removeNote(int index) {
        List<Note> notes = this.getAllNotes();
        notes.remove(index);
        this.put(ParseKeywords.BOARD_NOTE_ARRAY, notes);
    }


    /**
     *
     * @param jsonBoard
     * @return
     * @throws JSONException
     */
    public static BulletinBoard createFromJSON(JSONObject jsonBoard) throws JSONException {
        BulletinBoard board = BulletinBoard.createPersonalBoard(jsonBoard.getString(ParseKeywords.BOARD_NAME));
        JSONArray noteArray = jsonBoard.getJSONArray(ParseKeywords.BOARD_NOTE_ARRAY);
        for (int i = 0; i < noteArray.length(); ++i) {
            Note note = Note.createFromJSON(noteArray.getJSONObject(i));
            board.addNote(note);
        }
        return board;
    }


    /**
     *
     * @param currentBoard
     * @return
     * @throws JSONException
     */
    public static JSONObject writeToJSON(BulletinBoard currentBoard) throws JSONException {
        JSONObject personalBoard = new JSONObject();
        List<Note> notes = currentBoard.getAllNotes();
        JSONArray noteArray = new JSONArray();
        for (int i = 0; i < notes.size(); ++i) {
            JSONObject jsonNote = Note.writeToJSON(notes.get(i));
            noteArray.put(i, jsonNote);
        }
        personalBoard.put(ParseKeywords.BOARD_NOTE_ARRAY, noteArray);
        personalBoard.put(ParseKeywords.BOARD_NAME, currentBoard.getBoardName());
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
        return this.getBoardName().equals(otherBoard.getBoardName()) &&
               this.getObjectId().equals(otherBoard.getObjectId()) &&
               (this.getUpdatedAt().compareTo(otherBoard.getUpdatedAt()) == 0);
    }


    /**
     *
     * @return
     */
    private List<Note> getNoteArray() {
        return (ArrayList<Note>) this.get(ParseKeywords.BOARD_NOTE_ARRAY);
    }


    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ParseKeywords.BOARD_NAME);
        sb.append(": ");
        sb.append(this.getBoardName());
        List<Note> notes = this.getAllNotes();
        for (int i = 0; i < notes.size(); ++i) {
            if (i < notes.size() - 1) {
                sb.append(System.getProperty("line.separator"));
            }
            sb.append(notes.get(i).toString());
        }
        return sb.toString();
    }

    public void setPersonalBoard(boolean personalBoard) {
        this.isPersonalBoard = personalBoard;
    }
}
