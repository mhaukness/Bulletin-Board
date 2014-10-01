package team6.cs121.bulletinboard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alobb on 9/28/14.
 */
public class BulletinBoard implements Serializable {

    private String name;
    private List<Note> notes;


    /**
     * Create a new Bulletin Board
     * @param name
     */
    public BulletinBoard(String name) {
        this.notes = new ArrayList<Note>();
        this.name = name;
    }


    public static byte[] serialize(BulletinBoard bulletinBoard) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(bulletinBoard);
        os.close();
        return out.toByteArray();
    }

    public static BulletinBoard deserialize(InputStream data) throws IOException, ClassNotFoundException {
        int objDataLen = data.available();
        byte[] objData = new byte[objDataLen];
        data.read(objData);
        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(objData));
        return (BulletinBoard) oin.readObject();
    }


    /**
     *
     * @param note
     */
    public void addNote(Note note) {
        this.notes.add(0, note);
    }


    /**
     *
     * @param index
     * @return
     */
    public Note getNote(int index) {
        return this.notes.get(index);
    }


    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }


    /**
     *
     * @return
     */
    public List<Note> getAllNotes() {
        return  this.notes;
    }

}
