package team6.cs121.bulletinboard;

import android.content.Context;
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

import team6.cs121.bulletinboard.Model.BulletinBoard;

/**
 * Created by alobb on 10/15/14.
 */
public class PersonalBoardController extends BoardController {


    @Override
    protected void initBoards() {
        File file = new File(this.getFilesDir(), FILE_NAME);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = this.openFileInput(FILE_NAME);
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
                    fis.close();
                } catch (IOException e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        } else {
            this.currentBoard = new BulletinBoard("Personal Board");
        }
    }

    @Override
    public void save() {
        try {
            FileOutputStream fos = openFileOutput(this.FILE_NAME, Context.MODE_PRIVATE);
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
