package com.nphan.android.harvardartmuseum;

import android.content.Context;
import android.preference.PreferenceManager;

public class CultureSharedPreferences {
    private static final String PREF_CULTURE_NAME = "culture_name";
    private static final String PREF_CULTURE_ID = "culture_id";

    public static String getStoredCultureName(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_CULTURE_NAME, null);
    }

    public static void setStoredCultureName(Context context, String cultureName) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CULTURE_NAME, cultureName)
                .apply();
    }

    public static String getStoredCultureId(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_CULTURE_ID, null);
    }

    public static void setStoredCultureId(Context context, String cultureId) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CULTURE_ID, cultureId)
                .apply();
    }
}
