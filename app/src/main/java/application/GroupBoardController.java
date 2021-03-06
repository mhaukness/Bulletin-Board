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
 * This class acts as a controller for all boards that are stored in Parse.com and subsequently can
 *  be seen by a group of people.
 */
public class GroupBoardController extends BoardController {


    /**
     * Saves the board currently showing into Parse.com
     */
    @Override
    public void save() {
        if (this.currentBoard.isDirty()) {
            Intent serviceIntent = new Intent(this, DataDownloadService.class);
            mReceiver = new DataDownloadReceiver(new Handler());
            mReceiver.setReceiver(this);
            BoardHolderSingleton.getBoardHolder().setBoardToSave(this.currentBoard);
            serviceIntent.putExtra(DataDownloadService.SAVE_FLAG, true);
            serviceIntent.putExtra(DataDownloadReceiver.RECEIVER_FLAG, mReceiver);
            this.startService(serviceIntent);
        }
    }


    /**
     * @see application.BoardController#onCreate(android.os.Bundle)
     * This function also sets the title of the activity to be the current board name
     * @param savedInstanceState The saved state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(this.currentBoard.getBoardName());
        this.boardAdapter.notifyDataSetChanged();
    }


    /**
     * Initializes the board from the list of boards, unless it is a new board; in that case, the
     *  new board is created and saved into Parse right away.
     * @param extras The intent data sent when this activity was created
     */
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
}
