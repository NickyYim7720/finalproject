package com.nickyyim7720.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Model {

    private static String TAG = "Model.java===>";

    // ==== SharedPreferences ====
    public static void setPref(String key, String value, Context context) {
        Log.d(TAG, "setPref(key=" + key + ",value=" + value + ",Context=" + context + ")");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPref(String key, Context context) {
        Log.d(TAG, "getPref(key=" + key + ",Context=" + context + ")");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }


    public static String parseJSON(String json) {
        String out = json;
        Log.d(TAG, "parseJson(in)->" + json);
        Log.d(TAG, "parseJson(out)->" + out);
        return out;
    }

}
