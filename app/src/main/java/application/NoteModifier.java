package application;

/**
 * Created by alobb on 10/6/14.
 * This interface creates a contract that all Activities that use {@link application.model.BulletinBoard}
 *  need to implement.  The click listeners call these methods when certain actions are taken (For
 *  example, clicking a delete button, or a long click).
 */
public interface NoteModifier {

    /**
     * Remove the note from the bulletin board
     * @param index The index of the note
     */
    public void removeNote(int index);


    /**
     * Edit the note on the bulletin board
     * @param index The index of the note
     */
    public void editNote(int index);
}
