package com.koshka.origami.utils.ui.theme;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StyleRes;
import android.view.View;

import com.example.logic.algorithms.OrigamiThemePickAlgorithm;
import com.firebase.ui.auth.AuthUI;
import com.koshka.origami.R;
import com.koshka.origami.utils.SharedPrefs;

import java.util.ArrayList;

/**
 * Created by qm0937 on 10/6/16.
 */

public class OrigamiThemeHelper {

    private Activity activity;

    private int randomPickedTheme;

    private FirebaseThemeTranslator translator;

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";

    public OrigamiThemeHelper(Activity activity) {
        this.activity = activity;
        translator = new FirebaseThemeTranslator();
        randomThemeSetAndSave();
    }

    public OrigamiThemeHelper(Activity activity, int theme) {
        this.activity = activity;
        translator = new FirebaseThemeTranslator();
        randomPickedTheme = theme;
        setPrefTheme();
    }

    public void randomThemeSetAndSave(){

        Resources res = activity.getResources();
        TypedArray day_background_array = res.obtainTypedArray(R.array.day_style);
        TypedArray night_background_array = res.obtainTypedArray(R.array.night_style);


        randomPickedTheme = OrigamiThemePickAlgorithm.pickTheme(day_background_array, night_background_array);

        SharedPrefs.saveTheme(activity, randomPickedTheme);
        SharedPrefs.changeTheme(activity);


    }

    public void setPrefTheme() {
        Resources res = activity.getResources();

        SharedPrefs.saveTheme(activity, randomPickedTheme);
        SharedPrefs.changeTheme(activity);
    }

    public int getRandomPickedTheme() {
        return randomPickedTheme;
    }

    public void setRandomPickedTheme(int randomPickedTheme) {
        this.randomPickedTheme = randomPickedTheme;
    }

    public void firebaseSetAndSaveTheme(int firebaseThemeInt){

        int themeId = translator.getThemeResourceId(firebaseThemeInt);
        int backgroundId = translator.getBackgroundResourceId(firebaseThemeInt);

        SharedPrefs.saveTheme(activity, themeId);

    }

    public void dynamicViewBackgroundSet(int firebaseBackgroundInt, View view){

        Drawable drawable = activity.getResources().getDrawable(translator.getBackgroundResourceId(firebaseBackgroundInt));
        view.setBackground(drawable);

    }

    @MainThread
    @StyleRes
    public int getSelectedTheme() {

        if (getRandomPickedTheme() != -1) {
            return getRandomPickedTheme();
        } else {
            return R.style.amethist_theme;
        }

    }

    @MainThread
    @DrawableRes
    public int getSelectedLogo() {
        return AuthUI.NO_LOGO;
    }

    @MainThread
    public String[] getSelectedProviders() {

        ArrayList<String> selectedProviders = new ArrayList<>();
        selectedProviders.add(AuthUI.EMAIL_PROVIDER);
        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @MainThread
    public String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
    }

}
