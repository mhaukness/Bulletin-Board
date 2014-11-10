package application;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import application.DataDownload.BoardHolderSingleton;
import application.model.BulletinBoard;

/**
 * Created by alobb on 11/9/14.
 */
public class BoardSettingsController extends Activity {
    private BulletinBoard board;
    private UserAdapter users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_settings);
        this.board = BoardHolderSingleton.getBoardHolder().getBoardToSave();
        this.users = new UserAdapter(this, R.layout.user, board);
        EditText boardName = (EditText) findViewById(R.id.board_name);
        boardName.setText(board.getBoardName());

        ListView userList = (ListView) findViewById(R.id.board_users_list);
        userList.setAdapter(this.users);
    }
}
