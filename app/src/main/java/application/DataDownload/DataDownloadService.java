package application.DataDownload;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import application.model.BulletinBoard;

/**
 * Created by alobb on 10/16/14.
 */
public class DataDownloadService extends IntentService {
    public static final String PARSE_NOTE_CLASS = "Note";
    public static final String PARSE_NOTE_VALUE = "noteValue";
    private final String PARSE_BOARDS_CLASS = "Board";
    private final String PARSE_BOARD = "bulletinBoard";
    public static final String BOARD_NAME = "boardName";
    public static final String BOARD_INTENT = "boardValues";
    public static final int STATUS_FINISHED = 1;
    public static final String SAVE_NEW_FLAG = "saveNew";
    public static final String SAVE_EDIT_FLAG = "saveEdit";
    public static final String BOARD_TO_SAVE = "boardToSave";
    public static final String BOARD_ID = "objectId";


    public DataDownloadService() {
        super(DataDownloadService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        if (intent.getBooleanExtra(SAVE_NEW_FLAG, false)) {
            BulletinBoard boardToSave = BoardHolderSingleton.getBoardHolder().getBoardToSave();
            this.saveNewData(boardToSave);
        } else if (intent.getBooleanExtra(SAVE_EDIT_FLAG, false)) {
            BulletinBoard boardToSave = BoardHolderSingleton.getBoardHolder().getBoardToSave();
            try {
                this.saveEdit(boardToSave);
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage(), e);
            } catch (JSONException e) {
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
            } catch (JSONException e) {
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
    private List<BulletinBoard> readBoards() throws ParseException, JSONException {
        ParseQuery<BulletinBoard> boardQuery = ParseQuery.getQuery(PARSE_BOARDS_CLASS);
        boardQuery.include(BulletinBoard.NOTE_KEY);
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
    public void saveEdit(BulletinBoard board) throws ParseException, JSONException {
        ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery(PARSE_BOARDS_CLASS);
        ParseObject parseBoard = boardQuery.get(board.getId());
        if (parseBoard.getUpdatedAt().after(board.getLastUpdate())) {
            // Board has been updated before it was synced on this side, do not save the changes
        } else {
            board.saveInBackground();
        }
    }


    /**
     * Save a new Bulletin Board to Parse.com
     * @param board
     */
    public void saveNewData(BulletinBoard board) {
        board.saveInBackground();
    }
}
