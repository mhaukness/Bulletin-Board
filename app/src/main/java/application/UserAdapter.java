package application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

import application.model.BulletinBoard;

/**
 * Created by alobb on 11/9/14.
 */
public class UserAdapter extends ArrayAdapter<ParseUser> {
    private final Context context;
    private BulletinBoard board;
    private List<ParseUser> users;


    public UserAdapter(Context context, int resource, BulletinBoard board) {
        super(context, resource);
        this.context = context;
        this.board = board;
        this.users = board.getModerators();
    }


    static class ViewHolder {
        protected TextView user;
        protected Button deleteButton;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user, parent, false);
            viewHolder = new ViewHolder();
            this.initializeViewHolder(viewHolder, convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.user.setText(this.users.get(position).getEmail());
        this.setTags(viewHolder, convertView, position);

        return convertView;
    }


    /**
     * @return The total number of elements to be displayed
     */
    @Override
    public int getCount() {
        if (this.users == null) {
            return 0;
        }
        return this.users.size();
    }


    /**
     * @param position The position of the item
     * @return The Note at the given position
     */
    @Override
    public ParseUser getItem(int position) {
        return this.users.get(position);
    }


    /**
     * @param position The position in the list
     * @return The position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Initializes the view holder by linking it to UI elements
     * @param viewHolder The view holder to initialize
     * @param rowView The base view
     */
    private void initializeViewHolder(ViewHolder viewHolder, View rowView) {
        viewHolder.deleteButton = (Button) rowView.findViewById(R.id.delete_button);
        viewHolder.user = (TextView) rowView.findViewById(R.id.user_text);
    }


    /**
     * Adds tags to the UI elements and base view
     * @param viewHolder The holder for the UI elements
     * @param rowView The base view
     * @param position The position in the list
     */
    private void setTags(ViewHolder viewHolder, View rowView, int position) {
        viewHolder.user.setTag(position);
        viewHolder.deleteButton.setTag(position);
        rowView.setTag(viewHolder);
    }
}
