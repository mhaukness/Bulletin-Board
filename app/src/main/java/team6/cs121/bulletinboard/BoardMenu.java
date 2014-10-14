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
 * Created by Marina
 */
public class BoardMenu extends Activity {

    private Button createBoardButton;

    private View.OnClickListener createBoardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_menu);

        this.createBoardButton = (Button) findViewById(R.id.createBoard);
        this.createBoardButton.setOnClickListener(createBoardListener);
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
