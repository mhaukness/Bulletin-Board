package team6.cs121.bulletinboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by alobb on 10/7/14.
 */
public class EditNote extends Activity {

    private EditText note;
    private Button finished;
    private View.OnClickListener finishedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnData();
        }
    };
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        Bundle extras = getIntent().getExtras();
        String text = "";
        if (extras != null) {
            Note editNote = (Note) extras.getSerializable(NoteModifier.NOTE_VALUE);
            text = editNote.getText();
            this.index = extras.getInt(NoteModifier.NOTE_INDEX);
        }

        this.note = (EditText) findViewById(R.id.note);
        this.note.setText(text);
        this.note.setSelection(this.note.getText().length());
        this.finished = (Button) findViewById(R.id.finish);
        this.finished.setOnClickListener(finishedListener);
    }


    /**
     *
     */
    private void returnData() {
        Intent data = new Intent();
        data.putExtra(NoteModifier.NOTE_VALUE, this.note.getText().toString());
        data.putExtra(NoteModifier.NOTE_INDEX, this.index);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        finish();
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
