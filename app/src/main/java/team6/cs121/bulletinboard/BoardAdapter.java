package team6.cs121.bulletinboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import team6.cs121.bulletinboard.Model.BulletinBoard;
import team6.cs121.bulletinboard.Model.Note;
import team6.cs121.bulletinboard.Model.NoteModifier;

/**
 * Created by alobb on 9/29/14.
 */
public class BoardAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final BulletinBoard board;
    private final NoteModifier activity;


    public BoardAdapter(Context context, int resource, BulletinBoard board) {
        super(context, resource);
        this.context = context;
        this.board = board;
        this.activity = (NoteModifier) context;
    }

    static class ViewHolder {
        protected TextView text;
        protected Button deleteButton;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.note, parent, false);
        } else {
            rowView = convertView;
        }
        final ViewHolder viewHolder = new ViewHolder();
        NoteClickListener clickListener = new NoteClickListener(this.activity);

        viewHolder.text = (TextView) rowView.findViewById(R.id.note_textview);
        viewHolder.text.setText(this.board.getNote(position).getText());
        viewHolder.text.setOnClickListener(clickListener);
        viewHolder.text.setTag(position);

        viewHolder.deleteButton = (Button) rowView.findViewById(R.id.delete_button);
        viewHolder.deleteButton.setOnClickListener(clickListener);
        viewHolder.deleteButton.setTag(position);

        rowView.setTag(viewHolder);
        return rowView;
    }


    @Override
    public int getCount() {
        if (this.board == null) {
            return 0;
        }
        return this.board.numNotes();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Note getItem(int position) {
        return this.board.getNote(position);
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
