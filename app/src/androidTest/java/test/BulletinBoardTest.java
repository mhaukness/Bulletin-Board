package test;

import android.test.AndroidTestCase;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.Date;

import application.App;
import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 10/24/14.
 */
public class BulletinBoardTest extends AndroidTestCase {
    private static final String NAME = "boardName";
    private static final String ID = "123456";
    private static final Date LAST_UPDATE = new Date(100000);
    private static final String[] NOTE_TEXT = new String[]{"firstText", "secondText", "thirdText",
            "fourthText", "fifthText"};
    private static Note[] NOTES;


    protected void setUp() throws Exception {
        ParseObject.registerSubclass(BulletinBoard.class);
        ParseObject.registerSubclass(Note.class);
        Parse.initialize(getContext(), App.getApplicationId(), App.getClientKey());
        NOTES = new Note[NOTE_TEXT.length];
        for (int i = 0; i < NOTES.length; ++i) {
            NOTES[i] = new Note(NOTE_TEXT[i]);
        }
    }

    public void testConstructor() {
        BulletinBoard board = new BulletinBoard(NAME);
        assertEquals(NAME, board.getName());
        assertEquals(0, board.numNotes());
        assertTrue(board.getAllNotes().isEmpty());
    }


    public void testAdd() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        board.addNote(NOTES[0]);
        assertEquals(1, board.numNotes());
        assertEquals(NOTES[0], board.getNote(0));
        assertEquals(1, board.getAllNotes().size());
    }


    public void testBigAddRemove() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        int numIterations = 100;
        for (int i = 1; i <= numIterations; ++i) {
            board.addNote(NOTES[i % NOTES.length]);
            assertEquals(i, board.numNotes());
        }
        for (int i = 1; i <= numIterations; ++i) {
            board.removeNote(0);
            assertEquals(numIterations - i, board.numNotes());
        }
        assertEquals(0, board.numNotes());
        assertTrue(board.getAllNotes().isEmpty());
    }


    public void testGetNote() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        for (int i = NOTES.length - 1; i >= 0; --i) {
            board.addNote(NOTES[i]);
        }
        for (int i = 0; i < board.numNotes(); ++i) {
            Note note = board.getNote(i);
            assertEquals(board.getNote(i), NOTES[i]);
            assertEquals(board.getAllNotes().get(i), NOTES[i]);
        }
        for (int i = 0; i < NOTES.length; ++i) {
            assertEquals(board.getNote(i), NOTES[i]);
            assertEquals(board.getAllNotes().get(i), NOTES[i]);
        }
    }


    public void testGetAfterRemove() throws Exception {
        BulletinBoard board = new BulletinBoard(NAME);
        for (int i = NOTES.length - 1; i >= 0; --i) {
            board.addNote(NOTES[i]);
        }
        board.removeNote(2);
        assertEquals(NOTES.length - 1, board.numNotes());
        assertEquals(board.getNote(0), NOTES[0]);
        assertEquals(board.getNote(1), NOTES[1]);
        assertEquals(board.getNote(2), NOTES[3]);
        assertEquals(board.getNote(3), NOTES[4]);
    }


}
