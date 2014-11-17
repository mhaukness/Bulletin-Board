package application.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import application.DataDownload.ParseKeywords;

/**
 * Created by alobb on 9/28/14.
 * This class contains the definition of a board object; it contains the list of notes that show up
 *  on the board.  It extends ParseObject so that it is easy to save and load the data from Parse.com.
 */
@ParseClassName(ParseKeywords.BOARD_CLASS)
public class BulletinBoard extends ParseObject {


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
     *
     * @param index
     */
    public void removeNote(int index) {
        List<Note> notes = this.getAllNotes();
        notes.remove(index);
        this.put(ParseKeywords.BOARD_NOTE_ARRAY, notes);
    }

    /**
     * Removes a given note n from the board
     * @param n
     */
    public void removeNote(Note n) {
        List<Note> notes = this.getAllNotes();
        notes.remove(n);
        this.put(ParseKeywords.BOARD_NOTE_ARRAY, notes);
    }


    /**
     *
     * @param jsonBoard
     * @return
     * @throws JSONException
     */
    public static BulletinBoard createFromJSON(JSONObject jsonBoard) throws JSONException {
        BulletinBoard board = new BulletinBoard(jsonBoard.getString(ParseKeywords.BOARD_NAME));
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
}
