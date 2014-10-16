package team6.cs121.bulletinboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.Parse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alobb on 10/15/14.
 */
public abstract class BoardController extends Activity implements NoteModifier {

    private Button addNote;
    private Button addBoard;
    private ListView noteList;
    private ListView boardList;
    private List<BulletinBoard> boards;
    private EditText newNoteText;
    private EditText newBoardText;
    protected BulletinBoard currentBoard;
    private NoteAdapter noteAdapter;
    protected final String FILE_NAME = "currentBoard";

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
    private static final int EDIT_NOTE = 1;
    private JSONArray JSONBoards;


    protected void addBoard(BulletinBoard board) {
        this.boards.add(board);
    }


    /**
     * Initializes the bulletin board by attempting to load a file that contains the data for the
     * personal bulletin board.  If it cannot find the file, it will create a blank board.
     */
    protected void initBoards() {
        this.boards = new ArrayList<BulletinBoard>();
    }


    public void createNewBoard() {
        EditText title = (EditText) findViewById(R.id.newBoardText);
        if (!title.getText().toString().isEmpty()) {
            Intent i = new Intent(this,GroupBoardController.class);
            i.putExtra(BulletinBoard.BOARD_NAME, title.getText().toString());
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
            this.currentBoard.addNote(note);
            this.newNoteText.setText("");
            this.noteAdapter.notifyDataSetChanged();
        }
    }


    public void removeNote(int index) {
        this.currentBoard.removeNote(index);
        this.noteAdapter.notifyDataSetChanged();
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


    /////////////////////////////
    // Lifecycle Methods
    /////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);
        Parse.initialize(this, "zLVIHD2pn243N9DhZFqDGXQrYRtqpjOqUCq1nKqq",
                "IrVmsoQqhycibo4TNGGG36vZ8k9rrorWoaZpsdCU");

        initBoards();
        this.noteAdapter = new NoteAdapter(this, R.layout.note, currentBoard.getAllNotes());
        this.newNoteText = (EditText) findViewById(R.id.newNoteText);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.noteList.setAdapter(noteAdapter);
        this.addNote = (Button) findViewById(R.id.createNote);
        this.addNote.setOnClickListener(this.clickListener);
        this.addBoard = (Button) findViewById(R.id.createBoard);
        this.addBoard.setOnClickListener(this.clickListener);

        this.boardList = (ListView) findViewById(R.id.board_listview);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra(NoteModifier.NOTE_INDEX, -1);
                if (index != -1) {
                    Note note = this.currentBoard.getNote(index);
                    String newText = data.getStringExtra(NoteModifier.NOTE_VALUE);
                    note.editText(newText);
                    this.noteAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    /////////////////////////////
    // Android Boilerplate
    /////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
