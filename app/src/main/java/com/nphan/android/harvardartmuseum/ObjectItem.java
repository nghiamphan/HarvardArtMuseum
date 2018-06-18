package com.nphan.android.harvardartmuseum;

public class ObjectItem {
    private String mTitle;
    private String mPrimaryImageUrl;
    private String mDated;
    private String mPeriod;
    private String mMedium;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getPrimaryImageUrl() {
        return mPrimaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.mPrimaryImageUrl = primaryImageUrl;
    }

    public String getDated() {
        return mDated;
    }

    public void setDated(String dated) {
        if (dated.equals("null")) {
            dated = "unknown";
        }
        this.mDated = dated;
    }

    public String getPeriod() {
        return mPeriod;
    }

    public void setPeriod(String period) {
        if (period.equals("null")) {
            period = "unknown";
        }
        this.mPeriod = period;
    }

    public String getMedium() {
        return mMedium;
    }

    public void setMedium(String medium) {
        if (medium.equals("null")) {
            medium = "unknown";
        }
        this.mMedium = medium;
    }
}
