package team6.cs121.bulletinboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard {

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


    /**
     *
     * @param index
     */
    public void removeNote(int index) {
        this.notes.remove(index);
    }



    public static BulletinBoard createFromJSON(JSONObject boardArray) throws JSONException {
        BulletinBoard board = new BulletinBoard(boardArray.getString(BOARD_NAME));
        JSONArray noteArray = boardArray.getJSONArray(NOTE_KEY);
        for (int i = 0; i < noteArray.length(); ++i) {
            Note note = new Note(noteArray.getString(i));
            board.addNote(note);
        }
        return board;
    }

    public static JSONObject writeToJSON(BulletinBoard currentBoard) throws JSONException {
        JSONObject personalBoard = new JSONObject();
        List<Note> notes = currentBoard.getAllNotes();
        JSONArray noteArray = new JSONArray();
        for (int i = 0; i < notes.size(); ++i) {
            noteArray.put(i, notes.get(i).getText());
        }
        personalBoard.put(NOTE_KEY, noteArray);
        personalBoard.put(BOARD_NAME, currentBoard.getName());
        return personalBoard;
    }
}
