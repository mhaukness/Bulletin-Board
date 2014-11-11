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
import android.widget.Toast;

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
 * Note that this file is divided into regions; these regions are used to categorize methods and
 *  variables so that it is easy to find what the developer needs quickly.
 */
public abstract class BoardController extends Activity implements NoteModifier, DataReceiver,
        FragmentCallback {

    //region UI Elements
    private EditText newNoteText;
    private Note noteToEdit;
    //endregion


    //region Data
    protected List<BulletinBoard> boards;
    protected BulletinBoard currentBoard;
    //endregion


    //region Controller Elements
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
                case R.id.remove_board:

                    removeBoard();
                    break;
            }
        }
    };
    //endregion


    //region Intent Flags
    protected static final String NEW_BOARD_FLAG = "newBoard";
    protected static final String BOARD_INDEX_FLAG = "boardIndex";
    //endregion


    //region Board Modification
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

    public void removeBoard() {
        this.boards.remove(currentBoard);
        BoardHolderSingleton.getBoardHolder().setBoards(this.boards);
        BoardHolderSingleton.getBoardHolder().setBoardToDelete(currentBoard);
        Intent serviceIntent = new Intent(this, DataDownloadService.class);
        serviceIntent.putExtra(DataDownloadReceiver.RECEIVER_FLAG, mReceiver);
        serviceIntent.putExtra(DataDownloadService.DELETE_FLAG, true);
        this.startService(serviceIntent);
        super.finish();
    }
    //endregion


    //region Note Modification
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
     * Begin editing a note by displaying {@link application.EditFragment}.
     * @param index The index of the note
     */
    public void startEditNote(int index) {
        this.noteToEdit = this.currentBoard.getNote(index);
        this.noteToEdit.setBeingEdited(true);
        EditFragment editFragment = showEditFragment();
        editFragment.setNoteText(this.noteToEdit.getText());
        this.noteToEdit.setBeingEdited(true);
        this.boardAdapter.notifyDataSetChanged();
    }


    /**
     * Finish editing the note by hiding {@link application.EditFragment} and saving the new text.
     * @param fragment The fragment that is handling the editing of a note
     */
    public void finishEditNote(Fragment fragment) {
        hideEditFragment();
        this.noteToEdit.setText(((EditFragment) fragment).getNoteText());
        this.noteToEdit.setBeingEdited(false);
        this.boardAdapter.notifyDataSetChanged();
        this.noteToEdit = null;
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


    // endregion


    //region Data Integrity
    /**
     * Refresh the data by starting {@link application.DataDownload.DataDownloadService}
     */
    private void refreshData() {
        Intent serviceIntent = new Intent(this, DataDownloadService.class);
        serviceIntent.putExtra(DataDownloadReceiver.RECEIVER_FLAG, mReceiver);
        serviceIntent.putExtra(DataDownloadService.LOAD_FLAG, true);
        this.startService(serviceIntent);
    }


    /**
     * Initialize the list of boards
     */
    protected void initBoards(Bundle extras) {
        this.boards = BoardHolderSingleton.getBoardHolder().getAllBoards();
    }


    /**
     * Save the board in whatever method deemed appropriate by the class that implements this.
     */
    public abstract void save();
    //endregion


    //region Callbacks
    /**
     * This function is called when {@link application.DataDownload.DataDownloadService} is finished
     *  with whatever task it was given.  If it finished loading the boards, then they will now
     *  be shown in the options menu.
     * @param resultCode The code sent by {@link application.DataDownload.DataDownloadService}
     * @param data The bundle sent by {@link application.DataDownload.DataDownloadService}
     */
    @Override
    public void onReceiveResult(int resultCode, Bundle data) {
        switch (resultCode) {
            case DataDownloadService.LOAD_FINISHED:
                this.boards = BoardHolderSingleton.getBoardHolder().getAllBoards();
                invalidateOptionsMenu();
                Toast.makeText(this, "Data has been refreshed", Toast.LENGTH_SHORT).show();
                break;
            case DataDownloadService.SAVE_FINISHED:
                Toast.makeText(this, "Your data is successfully saved", Toast.LENGTH_SHORT).show();
                break;
            case DataDownloadService.LOAD_FAILED:
                Toast.makeText(this, "The data failed to load", Toast.LENGTH_SHORT).show();
                break;
            case DataDownloadService.SAVE_FAILED:
                Toast.makeText(this, "Your data failed to save", Toast.LENGTH_SHORT).show();
                break;
        }
    }



    /**
     * A callback that is called when a fragment finishes its work
     * @param fragment The fragment that finished
     */
    public void fragmentFinished(Fragment fragment) {
        if (fragment instanceof EditFragment) {
            finishEditNote(fragment);
        }
    }
    //endregion


    //region Lifecycle
    /**
     * Set up the display and initialize the boards for this controller
     * @param savedInstanceState The saved state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);
        hideEditFragment();
        initBoards(getIntent().getExtras());
        this.boardAdapter = new BoardAdapter(this, R.layout.note, this.currentBoard);
        this.newNoteText = (EditText) findViewById(R.id.new_note_text);

        ListView noteList = (ListView) findViewById(R.id.note_listview);
        noteList.setAdapter(boardAdapter);
        Button addNote = (Button) findViewById(R.id.create_note);
        Button delNote = (Button) findViewById(R.id.remove_board);
        delNote.setOnClickListener(this.buttonClickListener);
        addNote.setOnClickListener(this.buttonClickListener);
    }


    /**
     * Refresh the data when this activity is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new DataDownloadReceiver(new Handler());
        mReceiver.setReceiver(this);
        this.refreshData();
    }


    /**
     * Save the data when the activity is paused and set the service receiver to null to avoid
     *  leaking memory.
     */
    @Override
    protected void onPause() {
        super.onPause();
        save();
        mReceiver.setReceiver(null);
    }


    /**
     * Make sure that no notes are still set to being edited.
     */
    @Override
    protected void onStop() {
        super.onStop();
        List<Note> currNotes = this.currentBoard.getAllNotes();
        for (Note note : currNotes) {
            note.setBeingEdited(false);
        }
    }
    //endregion


    // region Action Bar
    /**
     * Add the other boards to the menu if they are not already on there
     * @param menu The menu to add items to
     * @return true so that the menu is created
     */
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


    /**
     * This function is called when an item in the action bar is pressed
     * @param item The item that was clicked on
     * @return {@link android.app.Activity#onOptionsItemSelected(android.view.MenuItem)}
     */
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
            i.putExtra(BoardController.BOARD_INDEX_FLAG, boardIndex);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    // endregion
}
