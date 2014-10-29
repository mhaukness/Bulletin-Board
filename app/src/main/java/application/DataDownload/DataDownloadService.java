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
 * This class contains a service that handles saving and loading data from Parse.com
 */
public class DataDownloadService extends IntentService {

    /**
     * A result code sent by the receiver of this service that indicates the service has finished
     *  loading the boards from Parse.com
     */
    public static final int LOAD_FINISHED = 1;

    /**
     * A result code sent by the receiver of this service that indicates the service has finished
     *  saving the board to Parse.com
     */
    public static final int SAVE_FINISHED = 2;

    /**
     * A result code sent by the receiver of this service that indicates the service has had an
     *  issue with loading the boards from Parse.com
     */
    public static final int LOAD_FAILED = 3;

    /**
     * A result code sent by the receiver of this service that indicates the service has had an
     *  issue with saving the board to Parse.com
     */
    public static final int SAVE_FAILED = 4;

    /**
     * A flag added to the intent that calls this service that indicates that the service should
     *  save the {@link application.model.BulletinBoard} to Parse.com
     */
    public static final String SAVE_FLAG = "save";


    /**
     * A flag added to the intent that calls this service that indicates that the service should
     *  load the {@link application.model.BulletinBoard}'s from Parse.com
     */
    public static final String LOAD_FLAG = "load";


    /**
     * Default constructor, necessary for a service.  Don't need to call this.
     */
    public DataDownloadService() {
        super(DataDownloadService.class.getName());
    }


    /**
     * This function is called when the service started.
     * If {@link application.DataDownload.DataDownloadService#SAVE_FLAG} is set to true, it will
     *  save the board from {@link BoardHolderSingleton#getBoardToSave()} to Parse.com
     * If {@link application.DataDownload.DataDownloadService#LOAD_FLAG} is set to true, it will
     *  load the boards from Parse.com and put them in {@link application.DataDownload.BoardHolderSingleton}
     * @param intent The intent that started this service
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = intent.getParcelableExtra(DataDownloadReceiver.RECEIVER_FLAG);
        if (intent.getBooleanExtra(SAVE_FLAG, false)) {
            BulletinBoard boardToSave = BoardHolderSingleton.getBoardHolder().getBoardToSave();
            try {
                this.save(boardToSave);
                receiver.send(SAVE_FINISHED, bundle);
            } catch (ParseException e) {
                receiver.send(SAVE_FAILED, bundle);
                Log.e("ERROR", e.getMessage(), e);
            }
        } else if (intent.getBooleanExtra(LOAD_FLAG, false)) {
            try {
                List<BulletinBoard> currBoards = this.readBoards();
                BoardHolderSingleton bh = BoardHolderSingleton.getBoardHolder();
                bh.setBoards(currBoards);
                receiver.send(LOAD_FINISHED, bundle);
            } catch (ParseException e) {
                receiver.send(LOAD_FAILED, bundle);
                Log.e("ERROR", e.getMessage(), e);
            }
        }
        this.stopSelf();
    }


    /**
     * Reads the boards from Parse.com
     * @return A list of Bulletin Boards
     * @throws ParseException {@link com.parse.ParseQuery#find()}
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
     * @param board The board to save to parse
     * @throws com.parse.ParseException {@link com.parse.ParseObject#saveInBackground()}
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
