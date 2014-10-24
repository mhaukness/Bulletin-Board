package team6.cs121.bulletinboard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import team6.cs121.bulletinboard.DataDownload.DataDownloadReceiver;
import team6.cs121.bulletinboard.DataDownload.DataDownloadService;
import team6.cs121.bulletinboard.Model.BulletinBoard;

/**
 * Created by alobb on 10/15/14.
 */
public class GroupBoardController extends BoardController {

    @Override
    public void save() {
        if (this.boardIsModified()) {
            Intent serviceIntent = new Intent(this, DataDownloadService.class);
            mReceiver = new DataDownloadReceiver(new Handler());
            mReceiver.setReceiver(this);
            serviceIntent.putExtra(DataDownloadService.BOARD_TO_SAVE, this.currentBoard);
            serviceIntent.putExtra(DataDownloadService.SAVE_DATA_FLAG, true);
            this.startService(serviceIntent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(BulletinBoard.BOARD_NAME)) {
                String name = extras.getString(BulletinBoard.BOARD_NAME);
                this.currentBoard = new BulletinBoard(name);
                this.addBoard(this.currentBoard);
            }
            if (extras.containsKey(this.NEW_BOARD_FLAG)) {
                this.boardModified = true;
                this.newBoard = true;
            }
            if (extras.containsKey(this.ALL_BOARD_FLAG)) {
                this.boards = extras.getParcelableArrayList(this.ALL_BOARD_FLAG);
            }
            if (extras.containsKey(this.BOARD_INDEX_FLAG)) {
                this.currentBoard = this.boards.get(extras.getInt(this.BOARD_INDEX_FLAG));
            }
        }
        setTitle(this.currentBoard.getName());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBoards() {

    }
}
