package com.nphan.android.harvardartmuseum;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    private static final String EXTRA_CULTURE_NAME = "com.nphan.android.harvardartmuseum.culture_name";
    private static final String EXTRA_CULTURE_ID = "com.nphan.android.harvardartmuseum.culture_id";

    public static Intent newIntent(Context context, String culture, String cultureId) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(EXTRA_CULTURE_NAME, culture);
        intent.putExtra(EXTRA_CULTURE_ID, cultureId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String  culture = getIntent().getStringExtra(EXTRA_CULTURE_NAME);
        String cultureId = getIntent().getStringExtra(EXTRA_CULTURE_ID);
        return PhotoGalleryFragment.newInstance(culture, cultureId);
    }
}
