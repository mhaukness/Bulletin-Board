package application;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import application.model.BulletinBoard;

/**
 * Created by alobb on 10/15/14.
 * This class defines a controller for a personal bulletin board.  This means that the data in this
 *  board will be saved locally; it will not be saved to Parse.com.
 */
public class PersonalBoardController extends BoardController {

    /**
     * The file name that the personal board is stored to.
     */
    private static final String SAVE_FILE_NAME = "personalBoard";


    /**
     * Initializes the personal board by trying to load it from a file; if the file is not found or
     *  any exceptions are thrown, a new, blank board is created and displayed.
     * @param extras The bundle of extras passed when the activity was started.
     */
    @Override
    protected void initBoards(Bundle extras) {
        super.initBoards(extras);
        File file = new File(this.getFilesDir(), SAVE_FILE_NAME);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = this.openFileInput(SAVE_FILE_NAME);
                String jString = null;
                FileChannel fc = fis.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jString = Charset.defaultCharset().decode(bb).toString();
                JSONObject boardArray = new JSONObject(jString);
                this.currentBoard = BulletinBoard.createFromJSON(boardArray);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                this.currentBoard = new BulletinBoard("Personal Board");
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        } else {
            this.currentBoard = new BulletinBoard("Personal Board");
        }
    }


    /**
     * Saves this board to the device.
     */
    @Override
    public void save() {
        try {
            FileOutputStream fos = openFileOutput(PersonalBoardController.SAVE_FILE_NAME, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            JSONObject array = BulletinBoard.writeToJSON(this.currentBoard);
            osw.write(array.toString());
            osw.close();
            fos.close();
        } catch (java.io.IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }
}
