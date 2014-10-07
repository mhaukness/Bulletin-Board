package team6.cs121.bulletinboard;

/**
 * Created by alobb on 10/6/14.
 */
public interface NoteModifier {
    public static String NOTE_VALUE = "NOTE_VALUE";

    public void removeNote(int index);

    public void editNote(int index);
}
