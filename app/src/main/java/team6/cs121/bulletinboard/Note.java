package team6.cs121.bulletinboard;

import java.io.Serializable;

/**
 * Created by alobb on 9/28/14.
 */
public class Note implements Serializable {

    private String text;
    // add some sort of id eventually
    //private String id;


    /**e
     * Create a new Note
     * @param
     */
    public Note() {
        this.text = "";
    }


    /**
     *
     * @param text
     */
    public Note(String text) {
        this.text = text;
    }


    public String getText() {
        return this.text;
    }


    /**
     *
     * @param newText
     */
    public void editText(String newText) {
        this.text = newText;
    }

}
