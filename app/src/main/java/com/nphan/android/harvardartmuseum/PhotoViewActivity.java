package com.nphan.android.harvardartmuseum;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class PhotoViewActivity extends SingleFragmentActivity {

    private static final String EXTRA_IMAGE_URL = "image_url";

    @Override
    protected Fragment createFragment() {
        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        return PhotoViewFragment.newInstance(imageUrl);
    }

    public static Intent newIntent(Context context, String imageUrl) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        return intent;
    }
}
