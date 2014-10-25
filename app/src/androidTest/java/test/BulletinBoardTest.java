package test;

import junit.framework.TestCase;

import java.util.Date;

import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 10/24/14.
 */
public class BulletinBoardTest extends TestCase {
    private static final String NAME = "boardName";
    private static final String ID = "123456";
    private static final Date LAST_UPDATE = new Date(100000);
    private static final String OLD_TEXT = "noteText";
    private static final Note NOTE = new Note(OLD_TEXT);


    public void testConstructor() {
        BulletinBoard board = new BulletinBoard(NAME);
        assertEquals(NAME, board.getName());
        assertEquals(0, board.numNotes());
        assertTrue(board.getAllNotes().isEmpty());
    }


    public void testAdd() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        board.addNote(NOTE);
        assertEquals(1, board.numNotes());
        assertEquals(NOTE, board.getNote(1));
        assertEquals(1, board.getAllNotes().size());
    }


    public void testNoteEquality() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        board.addNote(NOTE);
        assertEquals(board.getNote(1), NOTE);
        assertEquals(board.getAllNotes().get(1), NOTE);
    }
}
