package com.koshka.origami.utils.ui.theme;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.logic.algorithms.OrigamiThemePickAlgorithm;
import com.koshka.origami.R;
import com.koshka.origami.utils.SharedPrefs;

/**
 * Created by qm0937 on 10/6/16.
 */

public class OrigamiThemeHelper {

    private Activity activity;

    private int randomPickedTheme;

    private FirebaseThemeTranslator translator;

    public OrigamiThemeHelper(Activity activity) {
        this.activity = activity;
        translator = new FirebaseThemeTranslator();
    }

    public void randomThemeSetAndSave(){

        Resources res = activity.getResources();
        TypedArray day_background_array = res.obtainTypedArray(R.array.day_style);
        TypedArray night_background_array = res.obtainTypedArray(R.array.night_style);


        randomPickedTheme = OrigamiThemePickAlgorithm.pickTheme(day_background_array, night_background_array);

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


}
