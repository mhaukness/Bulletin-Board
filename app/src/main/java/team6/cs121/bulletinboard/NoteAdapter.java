package team6.cs121.bulletinboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alobb on 9/29/14.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final List<Note> values;
    private final NoteModifier activity;


    public NoteAdapter(Context context, int resource, List<Note> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
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
        viewHolder.text = (TextView) rowView.findViewById(R.id.note_textview);
        viewHolder.text.setText(this.values.get(position).getText());
        viewHolder.deleteButton = (Button) rowView.findViewById(R.id.delete_button);
        viewHolder.deleteButton.setOnClickListener(new NoteClickListener(this.activity));

        rowView.setTag(viewHolder);
        viewHolder.deleteButton.setTag(position);

        return rowView;
    }


    @Override
    public int getCount () {
        return values.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Note getItem(int position) {
        return values.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
