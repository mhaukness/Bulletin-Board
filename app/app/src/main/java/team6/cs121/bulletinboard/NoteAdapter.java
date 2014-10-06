package team6.cs121.bulletinboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alobb on 9/29/14.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private final List<Note> values;


    public NoteAdapter(Context context, int resource, List<Note> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.note, parent, false);
        EditText textField = (EditText) rowView.findViewById(R.id.note_textview);
        textField.setText(this.values.get(position).getText());
        // change the icon for Windows and iPhone

        return rowView;
    }

    @Override
    public int getCount () {
        return values.size();
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public Note getItem (int position) {
        return values.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
