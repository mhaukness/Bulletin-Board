package application.DataDownload;

import java.util.List;

import application.model.BulletinBoard;

/**
 * Created by alobb on 10/25/14.
 * This class uses the Singleton design pattern for passing data between
 *  {@link application.DataDownload.DataDownloadService} and the activities that want the Bulletin
 *  Boards.  The reason for using a Singleton instead of passing the Bulletin Boards as
 *  {@link android.os.Parcelable} objects through Intents is that the classes
 *  {@link application.model.BulletinBoard} and {@link application.model.Note} extend
 *  {@link com.parse.ParseObject}, which does not implement {@link android.os.Parcelable}
 */
public class BoardHolderSingleton {
    /**
     * The instance of this class that is returned
     */
    private static BoardHolderSingleton boardHolder;

    /**
     * The boards held by this singleton object.  These boards are set by
     *  {@link application.DataDownload.DataDownloadService} when it receives the boards from
     *  Parse.com.
     * The reason that the volatile keyword is on this variable is that we don't want it being
     *  written to and read from at the same time; this could lead to inconsistencies in the boards.
     */
    private volatile List<BulletinBoard> boards;

    /**
     * The board to save that is held by this singleton object.  This board is set by an activity
     *  that wants to save this board to Parse.com.
     * The reason that the volatile keyword is on this variable is that we don't want it being
     *  written to and read from at the same time; this could lead to inconsistencies in the boards.
     */
    private volatile BulletinBoard boardToSave;


    /**
     * A private constructor that is only called if the singleton object is equal to null.
     */
    private BoardHolderSingleton() {}


    /**
     * @return An instance of {@link application.DataDownload.BoardHolderSingleton}
     */
    public static BoardHolderSingleton getBoardHolder() {
        if (boardHolder == null) {
            boardHolder = new BoardHolderSingleton();
        }
        return boardHolder;
    }


    /**
     * Sets the list of boards that have been loaded by {@link application.DataDownload.DataDownloadService}
     * @param newBoards The recently-loaded boards
     */
    public void setBoards(List<BulletinBoard> newBoards) {
        this.boards = newBoards;
    }


    /**
     * @return The list of boards held by the singleton object
     */
    public List<BulletinBoard> getAllBoards() {
        return this.boards;
    }


    /**
     * @return The board that {@link application.DataDownload.DataDownloadService} needs to save
     */
    public BulletinBoard getBoardToSave() {
        return boardToSave;
    }


    /**
     * Sets the boars that needs to be saved by {@link application.DataDownload.DataDownloadService}
     * @param boardToSave To board that has been changed and needs to be saved
     */
    public void setBoardToSave(BulletinBoard boardToSave) {
        this.boardToSave = boardToSave;
    }
}
