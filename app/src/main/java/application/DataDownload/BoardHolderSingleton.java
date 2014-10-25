package application.DataDownload;

import java.util.List;

import application.model.BulletinBoard;

/**
 * Created by alobb on 10/25/14.
 */
public class BoardHolderSingleton {
    private static BoardHolderSingleton boardHolder;
    private List<BulletinBoard> boards;
    private BulletinBoard boardToSave;


    private BoardHolderSingleton() {

    }


    public static BoardHolderSingleton getBoardHolder() {
        if (boardHolder == null) {
            boardHolder = new BoardHolderSingleton();
        }
        return boardHolder;
    }


    public void setBoards(List<BulletinBoard> newBoards) {
        this.boards = newBoards;
    }


    public List<BulletinBoard> getAllBoards() {
        return this.boards;
    }


    public BulletinBoard getBoardToSave() {
        return boardToSave;
    }


    public void setBoardToSave(BulletinBoard boardToSave) {
        this.boardToSave = boardToSave;
    }
}
