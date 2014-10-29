package test;

import android.test.ActivityInstrumentationTestCase2;

import application.BoardController;

/**
 * Created by alobb on 10/29/14.
 */
public class BoardControllerTest extends ActivityInstrumentationTestCase2 {

    private BoardController mBoardController;


    public BoardControllerTest() {
        super(BoardController.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mBoardController = (BoardController) getActivity();
        setActivityInitialTouchMode(false);
    }
}
