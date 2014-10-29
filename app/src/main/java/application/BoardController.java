package application;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import application.DataDownload.BoardHolderSingleton;
import application.DataDownload.DataDownloadReceiver;
import application.DataDownload.DataDownloadService;
import application.DataDownload.DataReceiver;
import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 10/15/14.
 * This class contains the definition of a base controller for the board.  It should be overridden
 *  to provide definitions for the abstract methods based on the given situation.
 */
public abstract class BoardController extends Activity implements NoteModifier, DataReceiver,
        FragmentCallback {

    /////////////////////////////
    // UI Elements
    /////////////////////////////
    private Button addNote;
    private ListView noteList;
    private EditText newNoteText;
    private Note noteToEdit;

    /////////////////////////////
    // Data
    /////////////////////////////
    protected List<BulletinBoard> boards;
    protected BulletinBoard currentBoard;

    /////////////////////////////
    // Controller elements
    /////////////////////////////
    protected BoardAdapter boardAdapter;
    protected DataDownloadReceiver mReceiver;
    /**
     * Click listener for the buttons on the main screen
     */
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

    /////////////////////////////
    // Intent Flags
    /////////////////////////////
    protected static final String NEW_BOARD_FLAG = "newBoard";
    protected static final String BOARD_INDEX_FLAG = "boardIndex";


    /**
     * Add a board to the current list of boards
     * @param board The board to add
     */
    protected void addBoard(BulletinBoard board) {
        this.boards.add(board);
    }


    /**
     * Create a new board by starting the activity at {@link application.CreateBoard}
     */
    public void createNewBoard() {
        Intent i = new Intent(this, CreateBoard.class);
        i.putExtra(BoardController.NEW_BOARD_FLAG, true);
        startActivity(i);
    }


    /**
     * Creates a new note on the current board.  Will not create a note if the note EditText is empty.
     */
    private void createNote() {
        String text = this.newNoteText.getText().toString();
        if (!text.isEmpty()) {
            Note note = new Note(this.newNoteText.getText().toString());
            this.currentBoard.addNote(note);
            this.newNoteText.setText("");
            this.boardAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Removes a note from the board and redraws the list of notes.
     * @param index The index of the note to remove (Starting at 0)
     */
    public void removeNote(int index) {
        this.currentBoard.removeNote(index);
        this.boardAdapter.notifyDataSetChanged();
    }


    /**
     * Begin editing a note by displaying the {@link application.EditFragment}.
     * @param index The index of the note
     */
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
        this.boardAdapter = new BoardAdapter(this, R.layout.note, this.currentBoard);
        this.newNoteText = (EditText) findViewById(R.id.new_note_text);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.noteList.setAdapter(boardAdapter);
        this.addNote = (Button) findViewById(R.id.create_note);
        this.addNote.setOnClickListener(this.buttonClickListener);
    }


    /**
     * Display the {@link application.EditFragment} on the current activity.
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
     * Hide the {@link application.EditFragment} on the current activity.
     */
    private void hideEditFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.edit_fragment));
        ft.commit();
    }


    /**
     * A callback that is called when a fragment finishes its work
     * @param fragment The fragment that finished
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
        serviceIntent.putExtra(DataDownloadReceiver.RECEIVER_FLAG, mReceiver);
        serviceIntent.putExtra(DataDownloadService.LOAD_FLAG, true);
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
            case DataDownloadService.LOAD_FINISHED:
                this.boards = BoardHolderSingleton.getBoardHolder().getAllBoards();
                invalidateOptionsMenu();
        }
    }


    /////////////////////////////
    // Action Bar Methods
    /////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.boards == null) {
            return false;
        }
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
                break;
            case R.id.create_board:
                createNewBoard();
                break;
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
