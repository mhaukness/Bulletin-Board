package team6.cs121.bulletinboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import team6.cs121.bulletinboard.Model.BulletinBoard;

/**
 * Created by Marina on 10/12/2014.
 */
public class BoardAdapter extends ArrayAdapter<BulletinBoard> {

    private final Context context;
    private final List<BulletinBoard> values;

    public BoardAdapter(Context context, int resource, List<BulletinBoard> values) {
        super(context, resource);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.board, parent, false);
        } else {
            rowView = convertView;
        }
        TextView name = (TextView) rowView.findViewById(R.id.board_name);
        name.setText(values.get(position).getName());

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
    public BulletinBoard getItem(int position) {
        return values.get(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
