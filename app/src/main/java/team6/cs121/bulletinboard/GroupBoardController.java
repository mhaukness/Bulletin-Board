package team6.cs121.bulletinboard;

import android.os.Bundle;

/**
 * Created by alobb on 10/15/14.
 */
public class GroupBoardController extends BoardController {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        String name = "";
        if (extras != null) {
            name = extras.getString(BulletinBoard.BOARD_NAME);
            setTitle(name);
        }
        this.currentBoard = new BulletinBoard(name);
        super.onCreate(savedInstanceState);
    }
}
