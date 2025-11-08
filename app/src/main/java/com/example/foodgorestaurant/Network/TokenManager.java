package com.example.foodgorestaurant.Network;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_TYPE = "user_type";

    private static TokenManager instance;
    private final SharedPreferences prefs;

    private TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveToken(String token, String userType) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_TYPE, userType)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, null);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
