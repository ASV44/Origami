package com.koshka.origami.utils.ui.theme;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.koshka.origami.R;
import com.koshka.origami.utils.SharedPrefs;

/**
 * Created by qm0937 on 10/7/16.
 */

public class FirebaseThemeTranslator {

    public int getThemeResourceId(int firebaseBackgroundInt) {
        switch (firebaseBackgroundInt) {
            case 0: {
                return R.style.amethist_theme;
            }
            case 1: {
                return R.style.bloody_mary_theme;
            }
            case 2: {
                return R.style.influenza_theme;
            }
            case 3: {
                return R.style.shroom_theme;
            }
            case 4: {
                return R.style.kashmir_theme;
            }
            case 5: {
                return R.style.grapefruit_sunset_theme;
            }
            case 6: {
                return R.style.moonrise_theme;
            }
            case 7: {
                return R.style.purple_bliss_theme;
            }
            case 8: {
                return R.style.passion_theme;
            }
            case 9: {
                return R.style.little_leaf_theme;
            }
            case 10: {
                return R.style.reef_theme;
            }
            default:{
                return R.style.moonrise_theme;
            }
        }
    }

    public int getBackgroundResourceId(int firebaseBackgroundInt) {
        switch (firebaseBackgroundInt) {
            case 0: {
                return R.drawable.amethist_gradient;
            }
            case 1: {
                return R.drawable.bloody_mary_gradient;
            }
            case 2: {
                return R.drawable.influenza_gradient;
            }
            case 3: {
                return R.drawable.shroom_gradient;
            }
            case 4: {
                return R.drawable.kashmir_gradient;
            }
            case 5: {
                return R.drawable.grapefruit_sunset_gradient;
            }
            case 6: {
                return R.drawable.moonrise_gradient;
            }
            case 7: {
                return R.drawable.purple_bliss_gradient;
            }
            case 8: {
                return R.drawable.passion_gradient;
            }
            case 9: {
                return R.drawable.little_leaf_gradient;
            }
            case 10: {
                return R.style.reef_theme;
            }
            default:{
                return R.style.moonrise_theme;
            }
        }
    }
}
