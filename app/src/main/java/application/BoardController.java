package application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;

import java.util.List;

import application.DataDownload.BoardHolderSingleton;
import application.DataDownload.DataDownloadReceiver;
import application.DataDownload.DataDownloadService;
import application.model.BulletinBoard;
import application.model.Note;
import application.model.NoteModifier;

/**
 * Created by alobb on 10/15/14.
 */
public abstract class BoardController extends Activity implements NoteModifier, DataDownloadReceiver.Receiver {

    private Button addNote;
    private Button addBoard;
    private ListView noteList;
    private ListView boardList;
    protected List<BulletinBoard> boards;
    private EditText newNoteText;
    private EditText newBoardText;
    protected BulletinBoard currentBoard;
    protected BoardAdapter boardAdapter;
    protected final String FILE_NAME = "currentBoard";
    public final String PARSE_BOARDS = "BoardList";
    protected final String BOARDS = "boards";
    protected final String NEW_BOARD_FLAG = "newBoard";
    protected final String ALL_BOARD_FLAG = "allBoards";
    protected final String BOARD_INDEX_FLAG = "boardIndex";

    protected DataDownloadReceiver mReceiver;


    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.createNote:
                    createNote();
                    break;
                case R.id.createBoard:
                    createNewBoard();
                    break;
            }
        }
    };
    protected boolean newBoard = false;
    protected boolean boardModified = false;
    private static final int EDIT_NOTE = 1;


    protected void addBoard(BulletinBoard board) {
        this.boards.add(board);
    }


    public void createNewBoard() {
        EditText title = (EditText) findViewById(R.id.newBoardText);
        if (!title.getText().toString().isEmpty()) {
            Intent i = new Intent(this, GroupBoardController.class);
            i.putExtra(BulletinBoard.BOARD_NAME, title.getText().toString());
            i.putExtra(this.NEW_BOARD_FLAG, true);
            startActivity(i);
            title.setText("");
        }
    }


    /**
     *
     */
    private void createNote() {
        String text = this.newNoteText.getText().toString();
        if (!text.isEmpty()) {
            this.boardModified = true;
            Note note = new Note(this.newNoteText.getText().toString());
            try {
                this.currentBoard.addNote(note);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
            this.newNoteText.setText("");
            this.boardAdapter.notifyDataSetChanged();
        }
    }


    public void removeNote(int index) {
        this.boardModified = true;
        try {
            this.currentBoard.removeNote(index);
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
        this.boardAdapter.notifyDataSetChanged();
    }


    public void editNote(int index) {
        Intent i = new Intent(this, EditNote.class);
        try {
            i.putExtra(NoteModifier.NOTE_VALUE, Note.writeToJSON(this.currentBoard.getNote(index)).toString());
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
            return;
        }
        i.putExtra(NoteModifier.NOTE_INDEX, index);
        startActivityForResult(i, EDIT_NOTE);
    }


    protected boolean boardIsModified() {
        return this.boardModified;
    }


    /////////////////////////////
    // Lifecycle Methods
    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);
        initBoards(getIntent().getExtras());
        this.boardAdapter = new BoardAdapter(this, R.layout.note, this.currentBoard);
        this.newNoteText = (EditText) findViewById(R.id.newNoteText);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.noteList.setAdapter(boardAdapter);
        this.addNote = (Button) findViewById(R.id.createNote);
        this.addNote.setOnClickListener(this.clickListener);
        this.addBoard = (Button) findViewById(R.id.createBoard);
        this.addBoard.setOnClickListener(this.clickListener);
        this.boardList = (ListView) findViewById(R.id.board_listview);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new DataDownloadReceiver(new Handler());
        mReceiver.setReceiver(this);
        this.refreshData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        save();
        mReceiver.setReceiver(null);
    }


    private void refreshData() {
        Intent serviceIntent = new Intent(this, DataDownloadService.class);
        serviceIntent.putExtra(DataDownloadReceiver.RECEIVER, mReceiver);
        this.startService(serviceIntent);
    }


    /**
     *
     */
    protected void initBoards(Bundle extras) {
        this.boards = BoardHolderSingleton.getBoardHolder().getAllBoards();
    }


    /**
     *
     */
    public abstract void save();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra(NoteModifier.NOTE_INDEX, -1);
                if (index != -1) {
                    this.boardModified = true;
                    Note note = this.currentBoard.getNote(index);
                    String newText = data.getStringExtra(NoteModifier.NOTE_VALUE);
                    note.editText(newText);
                    this.boardAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case DataDownloadService.STATUS_FINISHED:
                List<BulletinBoard> newBoards = BoardHolderSingleton.getBoardHolder().getAllBoards();
                this.boards = newBoards;
                invalidateOptionsMenu();
        }
    }


    /////////////////////////////
    // Action Bar Methods
    /////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        final int NEW_MENU_ID = Menu.FIRST + 1;
        if (this.boards != null) {
            for (int i = 0; i < this.boards.size(); ++i) {
                menu.add(Menu.NONE, NEW_MENU_ID + i, i, this.boards.get(i).getName());
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.refresh:
                refreshData();
        }
        int firstBoard = Menu.FIRST + 1;
        int lastBoard = Menu.FIRST + this.boards.size();
        if (id >= firstBoard && id <= lastBoard) {
            int boardIndex = id - (Menu.FIRST + 1);
            // They want to switch to the board at boardIndex in this.boards
            Intent i = new Intent(this, GroupBoardController.class);
            i.putExtra(this.BOARD_INDEX_FLAG, boardIndex);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
