package hcmus.cnpm.team10.utils.bar_handler;

import android.app.ActionBar;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import hcmus.cnpm.team10.R;

public class ActionBarQuickSetUp {
    /*
        @brief Namespace to quickly support in setting action bar
    */

    private static final int DEFAULT_BAR_VIEW_ID = R.layout.bar_default;
    public static void setUpBar(AppCompatActivity activity, IBarHandler handler){
        ActionBar actionBar = activity.getActionBar();

        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(DEFAULT_BAR_VIEW_ID);

        View view = actionBar.getCustomView();
        handler.setUp(view);
    }

}
