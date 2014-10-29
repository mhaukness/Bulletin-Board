package application;

import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by alobb on 10/29/14.
 */
public class GroupBoardDispatchActivity extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return GroupBoardController.class;
    }
}
