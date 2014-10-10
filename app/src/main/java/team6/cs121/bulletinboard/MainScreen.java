package team6.cs121.bulletinboard;

import android.app.Activity;
import android.content.Context;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class MainScreen extends Activity implements NoteModifier {

    private Button addNote;
    private ListView noteList;
    private EditText newNoteText;
    private BulletinBoard personalBoard;
    private NoteAdapter adapter;
    private final String FILE_NAME = "personalBoard";
    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.createNote:
                    createNote();
                    break;
            }
        }
    };
    private static final int EDIT_NOTE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Parse.initialize(this, "zLVIHD2pn243N9DhZFqDGXQrYRtqpjOqUCq1nKqq", "IrVmsoQqhycibo4TNGGG36vZ8k9rrorWoaZpsdCU");


        initBoard();
        adapter = new NoteAdapter(this, R.layout.note, personalBoard.getAllNotes());
        this.newNoteText = (EditText) findViewById(R.id.newNoteText);
        this.noteList = (ListView) findViewById(R.id.note_listview);
        this.noteList.setAdapter(adapter);
        this.addNote = (Button) findViewById(R.id.createNote);
        this.addNote.setOnClickListener(this.clickListener);
    }


    /**
     *
     */
    private void initBoard() {
        File file = new File(this.getFilesDir(), FILE_NAME);
        if(file.exists()) {
            FileInputStream fis = null;
            try {
                fis = this.openFileInput(FILE_NAME);
                this.personalBoard = BulletinBoard.deserialize(fis);
            } catch (java.io.IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        } else {
            this.personalBoard = new BulletinBoard("Personal Board");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(BulletinBoard.serialize(this.personalBoard));
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }


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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     *
     */
    private void createNote() {
        String text = this.newNoteText.getText().toString();
        if (!text.isEmpty()) {
            Note note = new Note(this.newNoteText.getText().toString());
            this.personalBoard.addNote(note);
            this.newNoteText.setText("");
            this.adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void removeNote(int index) {
        this.personalBoard.removeNote(index);
        this.adapter.notifyDataSetChanged();
    }


    @Override
    public void editNote(int index) {
        Intent i = new Intent(this, EditNote.class);
        i.putExtra(NoteModifier.NOTE_VALUE, this.personalBoard.getNote(index));
        i.putExtra(NoteModifier.NOTE_INDEX, index);
        startActivityForResult(i, EDIT_NOTE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK) {
                int index = data.getIntExtra(NoteModifier.NOTE_INDEX, -1);
                if (index != -1) {
                    Note note = this.personalBoard.getNote(index);
                    String newText = data.getStringExtra(NoteModifier.NOTE_VALUE);
                    note.editText(newText);
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
