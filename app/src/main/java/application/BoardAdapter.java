package application;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import application.model.BulletinBoard;
import application.model.Note;

/**
 * Created by alobb on 9/29/14.
 * This class is used to display a board on the screen
 */
public class BoardAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private BulletinBoard board;
    private final NoteModifier editActivity;
    private final ConfirmDelete.DeleteDialogListener deleteActivity;


    /**
     * Create a new Board Adapter
     * @param context The activity that is creating this Board Adapter
     * @param resource The resource id that this adapter is creating
     * @param board The board that this adapter is displaying
     */
    public BoardAdapter(Context context, int resource, BulletinBoard board) {
        super(context, resource);
        this.context = context;
        this.board = board;
        this.editActivity = (NoteModifier) context;
        this.deleteActivity = (ConfirmDelete.DeleteDialogListener) context;
    }


    /**
     * This class is used to hold the views that are shown in an individual note row.
     */
    static class ViewHolder {
        protected TextView text;
        protected Button deleteButton;
        protected Button editButton;
    }


    /**
     * Gets the row view for the given position
     * @param position The current position
     * @param convertView A (possibly recycled) view
     * @param parent The parent of this view
     * @return The view to display for the given row
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.note, parent, false);
            viewHolder = new ViewHolder();
            this.initializeViewHolder(viewHolder, convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(this.getContext().getResources().getColor(R.color.white));

        this.setNoteClickListener(viewHolder);
        this.setTags(viewHolder, convertView, position);
        viewHolder.text.setText(this.board.getNote(position).getText());

        Resources resources = this.getContext().getResources();
        if (this.getItem(position).isBeingEdited()) {
            viewHolder.text.setBackgroundColor(resources.getColor(R.color.edit_background));
        } else {
            viewHolder.text.setBackgroundColor(resources.getColor(R.color.white));
        }

        return convertView;
    }


    /**
     * Initializes the view holder by linking it to UI elements
     * @param viewHolder The view holder to initialize
     * @param rowView The base view
     */
    private void initializeViewHolder(ViewHolder viewHolder, View rowView) {
        viewHolder.deleteButton = (Button) rowView.findViewById(R.id.delete_button);
        viewHolder.editButton = (Button) rowView.findViewById(R.id.edit_button);
        viewHolder.text = (TextView) rowView.findViewById(R.id.note_textview);
    }


    /**
     * Creates a click listener for the UI elements of the view holder
     * @param viewHolder The holder to assign a click listener to
     */
    private void setNoteClickListener(ViewHolder viewHolder) {
        NoteClickListener clickListener = new NoteClickListener(this.editActivity, this.deleteActivity);
        viewHolder.text.setOnClickListener(clickListener);
        viewHolder.deleteButton.setOnClickListener(clickListener);
        viewHolder.editButton.setOnClickListener(clickListener);
    }


    /**
     * Adds tags to the UI elements and base view
     * @param viewHolder The holder for the UI elements
     * @param rowView The base view
     * @param position The position in the list
     */
    private void setTags(ViewHolder viewHolder, View rowView, int position) {
        viewHolder.text.setTag(position);
        viewHolder.deleteButton.setTag(position);
        viewHolder.editButton.setTag(position);
        rowView.setTag(viewHolder);
    }


    /**
     * @return The total number of elements to be displayed
     */
    @Override
    public int getCount() {
        if (this.board == null) {
            return 0;
        }
        return this.board.numNotes();
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
     * @param position The position of the item
     * @return The Note at the given position
     */
    @Override
    public Note getItem(int position) {
        return this.board.getNote(position);
    }


    /**
     * Sets this adapter to display a new board and updates the UI accordingly
     * @param newBoard The new board to display
     */
    public void setNewBoard(BulletinBoard newBoard) {
        this.board = newBoard;
        this.notifyDataSetChanged();
    }
}
