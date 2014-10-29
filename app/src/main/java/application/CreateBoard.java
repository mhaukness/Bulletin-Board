package application;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import application.DataDownload.ParseKeywords;

/**
 * Created by eirikhansen on 10/27/14.
 */
public class CreateBoard extends Activity {

    private Button addBoard;


    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.create_board:
                    createNewBoard();
                    break;
            }
        }
    };

    public void createNewBoard(){
        EditText title = (EditText) findViewById(R.id.new_board_text);
        if (!title.getText().toString().isEmpty()) {
            Intent i = new Intent(this, GroupBoardController.class);
            i.putExtra(ParseKeywords.BOARD_NAME, title.getText().toString());
            i.putExtra(BoardController.NEW_BOARD_FLAG, true);
            startActivity(i);
            title.setText("");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_menu);
        this.addBoard = (Button) findViewById(R.id.create_board);
        this.addBoard.setOnClickListener(buttonClickListener);
    }
}
