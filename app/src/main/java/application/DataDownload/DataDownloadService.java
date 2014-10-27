package application.DataDownload;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import application.model.BulletinBoard;

/**
 * Created by alobb on 10/16/14.
 */
public class DataDownloadService extends IntentService {
    public static final int STATUS_FINISHED = 1;
    public static final String SAVE_FLAG = "save";


    public DataDownloadService() {
        super(DataDownloadService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        if (intent.getBooleanExtra(SAVE_FLAG, false)) {
            BulletinBoard boardToSave = BoardHolderSingleton.getBoardHolder().getBoardToSave();
            try {
                this.save(boardToSave);
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        } else {
            final ResultReceiver receiver = intent.getParcelableExtra(DataDownloadReceiver.RECEIVER);
            List<BulletinBoard> currBoards = null;
            try {
                currBoards = this.readBoards();
                BoardHolderSingleton bh = BoardHolderSingleton.getBoardHolder();
                bh.setBoards(currBoards);
                receiver.send(STATUS_FINISHED, bundle);
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
        this.stopSelf();
    }


    /**
     *
     * @return
     * @throws ParseException
     */
    private List<BulletinBoard> readBoards() throws ParseException {
        ParseQuery<BulletinBoard> boardQuery = ParseQuery.getQuery(ParseKeywords.BOARD_CLASS);
        boardQuery.include(ParseKeywords.BOARD_NOTE_ARRAY);
        List<BulletinBoard> boards = boardQuery.find();
        if (boards == null) {
            boards = new ArrayList<BulletinBoard>();
        }
        return boards;
    }


    /**
     * Save an edited board to Parse.com
     * @param board
     */
    public void save(BulletinBoard board) throws ParseException {
        if (board.getCreatedAt() == null) {
            // Board is new and has not been saved yet
            board.saveInBackground();
        } else {
            ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery(ParseKeywords.BOARD_CLASS);
            ParseObject parseBoard = boardQuery.get(board.getObjectId());
            if (parseBoard.getUpdatedAt().after(board.getUpdatedAt())) {
                // Board has been updated before it was synced on this side, do not save the changes
            } else {
                board.saveInBackground();
            }
        }
    }
}
