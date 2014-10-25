package team6.cs121.bulletinboard;

import org.junit.Test;

import java.util.Date;

import team6.cs121.bulletinboard.Model.BulletinBoard;

import static junit.framework.Assert.assertEquals;

/**
 * Created by alobb on 10/24/14.
 */
public class BulletinBoardTest {
    private static final String NAME = "boardName";
    private static final String ID = "123456";
    private static final Date LAST_UPDATE = new Date(100000);


    @Test
    public void ConstructorTest() {
        BulletinBoard board = new BulletinBoard(NAME);
        board.setId(ID);
        assertEquals(NAME, board.getName());
        assertEquals(ID, board.getId());
        assertEquals(LAST_UPDATE, board.getLastUpdate());
    }

}
