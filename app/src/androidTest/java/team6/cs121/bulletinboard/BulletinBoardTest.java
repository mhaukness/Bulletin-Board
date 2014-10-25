package team6.cs121.bulletinboard;

import android.os.Parcel;

import junit.framework.TestCase;

import java.util.Date;

import team6.cs121.bulletinboard.Model.BulletinBoard;

/**
 * Created by alobb on 10/24/14.
 */
public class BulletinBoardTest extends TestCase {
    private static final String NAME = "boardName";
    private static final String ID = "123456";
    private static final Date LAST_UPDATE = new Date(100000);


    public void testConstructor() {
        BulletinBoard board = new BulletinBoard(NAME);
        board.setId(ID);
        board.setLastUpdate(LAST_UPDATE);
        assertEquals(NAME, board.getName());
        assertEquals(ID, board.getId());
        assertEquals(LAST_UPDATE, board.getLastUpdate());
    }


    public void testParcelable() {
        BulletinBoard board = new BulletinBoard(NAME);
        board.setId(ID);
        board.setLastUpdate(LAST_UPDATE);
        Parcel parcel = Parcel.obtain();
        board.writeToParcel(parcel, 0);
        //done writing, now reset parcel for reading
        parcel.setDataPosition(0);
        //finish round trip
        BulletinBoard createFromParcel = BulletinBoard.CREATOR.createFromParcel(parcel);
        assertEquals(board, createFromParcel);
    }

}
