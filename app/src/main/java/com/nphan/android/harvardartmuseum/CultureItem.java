package com.nphan.android.harvardartmuseum;

import java.io.Serializable;

public class CultureItem implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;
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
