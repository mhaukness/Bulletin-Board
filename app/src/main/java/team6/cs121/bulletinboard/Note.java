package team6.cs121.bulletinboard;

import android.app.Activity;
import android.widget.TextView;

import com.example.marina.bulletin_board.R;

/**
 * Created by alobb on 9/28/14.
 */
public class Note extends Activity {

    TextView textView;

    private String text;
    // add some sort of id eventually
    //private String id;


    /**e
     * Create a new Note
     * @param
     */
    public Note() {
        this.text = "";
        textView = (TextView) findViewById(R.id.note_textview);
        textView.setText(this.text);
    }

    /**e
     * Create a new Note
     * @param new_text
     */
    public Note(String new_text) {
        this.text = new_text;
        textView = (TextView) findViewById(R.id.note_textview);
        textView.setText(this.text);
    }



}
