package team6.cs121.bulletinboard;

import android.view.View;
import android.widget.AdapterView;

import team6.cs121.bulletinboard.Model.BulletinBoard;
import team6.cs121.bulletinboard.Model.NoteModifier;


/**
 * Created by alobb on 9/30/14.
 */
public class BulletinBoardClickListener implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private BulletinBoard board;
    private NoteModifier activity;

    /**
     *
     */
    public BulletinBoardClickListener(BulletinBoard board) {
        this.board = board;
    }

    /**
     *
     */
    public BulletinBoardClickListener(BulletinBoard board, NoteModifier noteModifier) {
        this.board = board;
        this.activity = noteModifier;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.activity.removeNote(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.activity.editNote(position);
        return false;
    }
}
