package team6.cs121.bulletinboard;

import org.junit.Test;

import team6.cs121.bulletinboard.Model.Note;

import static junit.framework.Assert.assertEquals;

/**
 * Created by alobb on 10/24/14.
 */
public class NoteTest {
    private static final String OLD_TEXT = "noteText";
    private static final String NEW_TEXT = "newNoteText";
    private static final String ID = "123456";


    @Test
    public void ConstructorTest() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
        assertEquals(OLD_TEXT, note.getText());
        assertEquals(ID, note.getId());
    }


    @Test
    public void EditTest() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
        assertEquals(OLD_TEXT, note.getText());
        note.editText(NEW_TEXT);
        assertEquals(NEW_TEXT, note.getText());
    }


    @Test
    public void EditSameTest() {
        Note note = new Note(OLD_TEXT);
        note.setId(ID);
        assertEquals(OLD_TEXT, note.getText());
        note.editText(OLD_TEXT);
        assertEquals(OLD_TEXT, note.getText());
    }
}
