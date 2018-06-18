package com.nphan.android.harvardartmuseum;

public class CultureItem {
    private String mId;
    private String mCulture;

    @Override
    public String toString() {
        return mId + " " + mCulture;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCulture() {
        return mCulture;
    }

    public void setCulture(String culture) {
        mCulture = culture;
    }
}
