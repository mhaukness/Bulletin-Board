package team6.cs121.bulletinboard.DataDownload;

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

import team6.cs121.bulletinboard.Model.BulletinBoard;
import team6.cs121.bulletinboard.Model.Note;

/**
 * Created by alobb on 10/16/14.
 */
public class DataDownloadService extends IntentService {
    private static final String PARSE_NOTE_CLASS = "Note";
    public static final String PARSE_NOTE_VALUE = "noteValue";
    public static final String PARSE_NOTES = "boardNotes";
    private final String PARSE_BOARDS_CLASS = "Board";
    private final String PARSE_BOARD = "bulletinBoard";
    public static final String BOARD_NAME = "boardName";
    private ParseObject parseBoards;
    private List<BulletinBoard> boards;
    public static final String BOARD_INTENT = "boardValues";
    public static final int STATUS_FINISHED = 1;
    public static final String SAVE_DATA_FLAG = "saveData";
    public static final String BOARD_TO_SAVE = "boardToSave";


    public DataDownloadService() {
        super(DataDownloadService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        if (intent.getBooleanExtra(SAVE_DATA_FLAG, false)) {
            BulletinBoard boardToSave = intent.getParcelableExtra(BOARD_TO_SAVE);
            this.saveNewData(boardToSave);
        } else {
            final ResultReceiver receiver = intent.getParcelableExtra(DataDownloadReceiver.RECEIVER);
            List<BulletinBoard> currBoards = null;
            try {
                currBoards = this.readBoards();
                if (currBoards.size() > 0) {
                    bundle.putParcelableArrayList(BOARD_INTENT, (ArrayList<BulletinBoard>) currBoards);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (ParseException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
        this.stopSelf();
    }


    private void addBoard(BulletinBoard board) {
        this.boards.add(board);
    }


    private List<BulletinBoard> readBoards() throws ParseException {
        final List<BulletinBoard> currBoards = new ArrayList<BulletinBoard>();
        ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery(PARSE_BOARDS_CLASS);
        boardQuery.include(PARSE_NOTES);
        List<ParseObject> objects = boardQuery.find();
        for (int i = 0; i < objects.size(); ++i) {
            ParseObject currObject = objects.get(i);
            BulletinBoard currBoard = null;
            try {
                currBoard = BulletinBoard.createFromParse(currObject);
            } catch (JSONException jsonE) {
                Log.e("ERROR", jsonE.getMessage(), jsonE);
            } catch (ParseException parseE) {
                Log.e("ERROR", parseE.getMessage(), parseE);
            }
            currBoards.add(currBoard);
        }
        return currBoards;
    }


    /**
     * Save an edited board to Parse.com
     * @param boards
     */
    public void saveEdit(List<BulletinBoard> boards) {
        // TODO: Implement
    }


    /**
     * Save a new Bulletin Board to Parse.com
     * @param board
     */
    public void saveNewData(BulletinBoard board) {
        ParseObject parseBoard = new ParseObject(PARSE_BOARDS_CLASS);
        parseBoard.put(BOARD_NAME, board.getName());
        List<ParseObject> parseNotes = new ArrayList<ParseObject>();
        for (int i = 0; i < board.numNotes(); ++i) {
            Note currNote = board.getNote(i);
            ParseObject parseNote = new ParseObject(PARSE_NOTE_CLASS);
            parseNote.put(PARSE_NOTE_VALUE, currNote.getText());
            parseNotes.add(parseNote);
            parseNote.saveInBackground();
        }
        parseBoard.addAll(PARSE_NOTES, parseNotes);
        parseBoard.saveInBackground();
    }
}
