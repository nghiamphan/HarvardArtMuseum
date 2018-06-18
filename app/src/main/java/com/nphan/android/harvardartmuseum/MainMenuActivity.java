package com.nphan.android.harvardartmuseum;

import android.support.v4.app.Fragment;

public class MainMenuActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MainMenuFragment.newInstance();
    }
}
