package team6.cs121.bulletinboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard {

    private String name;
    private List<Note> notes;


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
        this.notes.add(note);
    }


    /**
     *
     * @param index
     * @return
     */
    public Note getNote(int index) {
        return this.notes.get(index);
    }
}
