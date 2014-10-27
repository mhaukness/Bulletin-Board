package application;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONException;

import java.util.List;

import application.DataDownload.BoardHolderSingleton;
import application.DataDownload.DataDownloadReceiver;
import application.DataDownload.DataDownloadService;
import application.DataDownload.DataReceiver;
import application.DataDownload.ParseKeywords;
import application.model.BulletinBoard;
import application.model.Note;
import application.model.NoteModifier;

/**
 * Created by alobb on 10/15/14.
 */
public abstract class BoardController extends Activity implements NoteModifier, DataReceiver, FragmentCallback {

    // UI Elements
    private LinearLayout boardView;
    private Button addNote;
    private Button addBoard;
    private ListView noteList;
    private EditText newNoteText;
    private EditText newBoardText;
    private Note noteToEdit;

    // Data
    protected List<BulletinBoard> boards;
    protected BulletinBoard currentBoard;

    protected BoardAdapter boardAdapter;
    protected final String FILE_NAME = "currentBoard";
    public final String PARSE_BOARDS = "BoardList";
    protected final String BOARDS = "boards";
    protected final String NEW_BOARD_FLAG = "newBoard";
    protected final String ALL_BOARD_FLAG = "allBoards";
    protected final String BOARD_INDEX_FLAG = "boardIndex";
    protected DataDownloadReceiver mReceiver;


    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_note:
                    createNote();
                    break;
                case R.id.create_board:
                    createNewBoard();
                    break;
            }
        }
    };


    protected void addBoard(BulletinBoard board) {
        this.boards.add(board);
    }


    public void createNewBoard() {
        EditText title = (EditText) findViewById(R.id.new_board_text);
        if (!title.getText().toString().isEmpty()) {
            Intent i = new Intent(this, GroupBoardController.class);
            i.putExtra(ParseKeywords.BOARD_NAME, title.getText().toString());
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
        this.currentBoard.removeNote(index);
        this.boardAdapter.notifyDataSetChanged();
    }


    public void editNote(int index) {
        this.noteToEdit = this.currentBoard.getNote(index);
        this.noteToEdit.setBeingEdited(true);
        EditFragment editFragment = showEditFragment();
        editFragment.setNoteText(this.noteToEdit.getText());
        this.noteToEdit.setBeingEdited(true);
        this.boardAdapter.notifyDataSetChanged();
    }


    /////////////////////////////
    // Lifecycle Methods
    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);
        hideEditFragment();
        initBoards(getIntent().getExtras());
        this.boardView = (LinearLayout) findViewById(R.id.board_view);
        this.boardAdapter = new BoardAdapter(this, R.layout.note, this.currentBoard);
        this.newNoteText = (EditText) findViewById(R.id.new_note_text);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.noteList.setAdapter(boardAdapter);
        this.addNote = (Button) findViewById(R.id.create_note);
        this.addNote.setOnClickListener(this.buttonClickListener);
        this.addBoard = (Button) findViewById(R.id.create_board);
        this.addBoard.setOnClickListener(this.buttonClickListener);
    }


    /**
     *
     */
    private EditFragment showEditFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EditFragment fragment = (EditFragment) fm.findFragmentById(R.id.edit_fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.show(fragment);
        ft.commit();
        return fragment;
    }


    /**
     *
     */
    private void hideEditFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.hide(fm.findFragmentById(R.id.edit_fragment));
        ft.commit();
    }


    /**
     *
     * @param fragment
     */
    public void fragmentFinished(Fragment fragment) {
        if (fragment instanceof EditFragment) {
            hideEditFragment();
            this.noteToEdit.editText(((EditFragment) fragment).getNoteText());
            this.noteToEdit.setBeingEdited(false);
            this.boardAdapter.notifyDataSetChanged();
            this.noteToEdit = null;
        }
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


    @Override
    protected void onStop() {
        super.onStop();
        List<Note> currNotes = this.currentBoard.getAllNotes();
        for (Note note : currNotes) {
            note.setBeingEdited(false);
        }
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
                menu.add(Menu.NONE, NEW_MENU_ID + i, i, this.boards.get(i).getBoardName());
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
            BoardHolderSingleton.getBoardHolder().setBoards(this.boards);
            Intent i = new Intent(this, GroupBoardController.class);
            i.putExtra(this.BOARD_INDEX_FLAG, boardIndex);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
