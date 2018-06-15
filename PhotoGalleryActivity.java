package com.nphan.android.harvardartmuseum;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    private static final String EXTRA_CULTURE_NAME = "com.nphan.android.harvardartmuseum.culture_name";

    public static Intent newIntent(Context context, String culture) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(EXTRA_CULTURE_NAME, culture);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String culture = getIntent().getStringExtra(EXTRA_CULTURE_NAME);
        return PhotoGalleryFragment.newInstance(culture);
    }
}
