package com.koshka.origami.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qm0937 on 10/5/16.
 */

public class SharedPrefs {


    public static final String SHARED_PREFS = "SharedPrefs";

    public static final String THEME = "theme";
    public static final String BACKGROUND = "gradient";
    public static final String OTHER = "";

    public static void changeBackground(Activity activity, View view) {
        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int backgroundGradient = prefs.getInt(BACKGROUND, -1);
        if (backgroundGradient != -1) {
            view.setBackground(activity.getResources().getDrawable(backgroundGradient));
        }
    }

    public static void changeTheme(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int theme = prefs.getInt("theme", -1);

        if (theme != -1) {
            activity.setTheme(theme);
        }
    }

    public static void changeViewsBackground(Activity activity, View... views) {

        SharedPreferences prefs = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int backgroundGradient = prefs.getInt(BACKGROUND, -1);



        if (backgroundGradient != -1) {
            for (View view : views) {
                if (view != null) {
                    view.setBackground(activity.getResources().getDrawable(backgroundGradient));
                }

            }
        }

    }

    public static void saveBackground(Activity activity, int code){
        saveInt(activity, BACKGROUND, code);
    }

    public static void saveTheme(Activity activity, int themeCode){
        saveInt(activity, THEME, themeCode);
    }

    private static void saveInt(Activity activity, String key, int value){
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
        editor.apply();
    }
}
