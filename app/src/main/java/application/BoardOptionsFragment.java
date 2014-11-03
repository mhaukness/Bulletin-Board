package application;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by alobb on 10/30/14.
 */
public class BoardOptionsFragment extends Fragment {
    private Button saveButton;
    private EditText boardName;
    private ListView boardUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_settings, container);
        this.saveButton = (Button) view.findViewById(R.id.save_board_settings);
        this.boardName = (EditText) view.findViewById(R.id.board_name);
        this.boardUsers = (ListView) view.findViewById(R.id.board_users_list);
        return view;
    }
}
