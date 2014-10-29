package application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import application.DataDownload.BoardHolderSingleton;
import application.DataDownload.DataDownloadReceiver;
import application.DataDownload.DataDownloadService;
import application.DataDownload.ParseKeywords;
import application.model.BulletinBoard;

/**
 * Created by alobb on 10/15/14.
 */
public class GroupBoardController extends BoardController {


    @Override
    public void save() {
        if (this.currentBoard.isDirty()) {
            Intent serviceIntent = new Intent(this, DataDownloadService.class);
            mReceiver = new DataDownloadReceiver(new Handler());
            mReceiver.setReceiver(this);
            BoardHolderSingleton.getBoardHolder().setBoardToSave(this.currentBoard);
            serviceIntent.putExtra(DataDownloadService.SAVE_FLAG, true);
            this.startService(serviceIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(this.currentBoard.getBoardName());
        this.boardAdapter.notifyDataSetChanged();
    }


    @Override
    protected void initBoards(Bundle extras) {
        super.initBoards(extras);
        if (extras != null) {
            if (extras.containsKey(this.NEW_BOARD_FLAG)) {
                if (extras.containsKey(ParseKeywords.BOARD_NAME)) {
                    String name = extras.getString(ParseKeywords.BOARD_NAME);
                    this.currentBoard = new BulletinBoard(name);
                    this.addBoard(this.currentBoard);
                    this.save();
                } else {
                    throw new IllegalArgumentException("A new board requires a name");
                }
            }
            if (extras.containsKey(this.BOARD_INDEX_FLAG)) {
                this.currentBoard = this.boards.get(extras.getInt(this.BOARD_INDEX_FLAG));
            }
        }
        this.invalidateOptionsMenu();
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        super.onReceiveResult(resultCode, data);
        for (int i = 0; i < this.boards.size(); ++i) {
            if (this.boards.get(i).getObjectId().equals(this.currentBoard.getObjectId())) {
                this.currentBoard = this.boards.get(i);
                this.boardAdapter.setNewBoard(this.currentBoard);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.save();
    }
}
