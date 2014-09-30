package team6.cs121.bulletinboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import team6.cs121.bulletinboard.R;

import java.util.List;


public class MainScreen extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    Button add_note_button;
    ListView note_listview;

    //List<BulletinBoard> boards;
    BulletinBoard personalBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        // Access the Button defined in layout XML
        // and listen for it here
        add_note_button = (Button) findViewById(R.id.add_note_button);
        add_note_button.setOnClickListener(this);


        // Access the ListView
        note_listview = (ListView) findViewById(R.id.note_listview);
        // Set this activity to react to list items being pressed
        note_listview.setOnItemClickListener(this);

        personalBoard = new BulletinBoard("Personal Board");
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.add_note_button) {
            //create new note layout
            // get text user enters
            // create new note
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
