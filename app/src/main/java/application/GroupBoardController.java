package application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import application.DataDownload.BoardHolderSingleton;
import application.DataDownload.DataDownloadReceiver;
import application.DataDownload.DataDownloadService;
import application.model.BulletinBoard;

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
            BoardHolderSingleton.getBoardHolder().setBoardToSave(this.currentBoard);
            if (this.newBoard) {
                serviceIntent.putExtra(DataDownloadService.SAVE_NEW_FLAG, true);
            } else {
                serviceIntent.putExtra(DataDownloadService.SAVE_EDIT_FLAG, true);
            }
            this.startService(serviceIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(this.currentBoard.getName());
        this.boardAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initBoards(Bundle extras) {
        super.initBoards(extras);
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
            if (extras.containsKey(this.BOARD_INDEX_FLAG)) {
                this.currentBoard = this.boards.get(extras.getInt(this.BOARD_INDEX_FLAG));
            }
        }
    }
}
