package com.koshka.origami.utils.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.koshka.origami.R;
import com.koshka.origami.utils.SharedPrefs;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qm0937 on 10/3/16.
 */

public class ViewThemeUtil {

    private Activity activity;
    private View view;

    public ViewThemeUtil(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    private Drawable getDrawableFromInt(int code) {
        return getActivity().getResources().getDrawable(code);
    }

    private void setBackground(int drawable) {
        view.setBackground(getDrawableFromInt(drawable));
    }

    public void setBackgroundToViewAndSavePreference(int firebaseBackgroundInt){
        switch (firebaseBackgroundInt) {
            case 0: {
                setBackground(R.drawable.amethist_gradient);
                putIntInSharedPreference(R.style.amethist_theme, R.drawable.amethist_gradient);
                break;
            }
            case 1: {
                setBackground(R.drawable.bloody_mary_gradient);
                putIntInSharedPreference(R.style.bloody_mary__theme, R.drawable.bloody_mary_gradient);
                break;
            }
            case 2: {
                setBackground(R.drawable.influenza_gradient);
                putIntInSharedPreference(R.style.influenza_theme, R.drawable.influenza_gradient);
                break;
            }
            case 3: {
                setBackground(R.drawable.shroom_gradient);
                putIntInSharedPreference(R.style.shroom_theme, R.drawable.shroom_gradient);
                break;
            }
            case 4: {
                setBackground(R.drawable.kashmir_gradient);
                putIntInSharedPreference(R.style.kashmir_theme, R.drawable.kashmir_gradient);
                break;
            }
            case 5: {
                setBackground(R.drawable.grapefruit_sunset_gradient);
                putIntInSharedPreference(R.style.grapefruit_sunset_theme, R.drawable.grapefruit_sunset_gradient);
                break;
            }
            case 6: {
                setBackground(R.drawable.moonrise_gradient);
                putIntInSharedPreference(R.style.moonrise_theme, R.drawable.moonrise_gradient);
                break;
            }
            case 7: {
                setBackground(R.drawable.purple_bliss_gradient);
                putIntInSharedPreference(R.style.purple_bliss_theme, R.drawable.purple_bliss_gradient);
                break;
            }
            case 8: {
                setBackground(R.drawable.passion_gradient);
                putIntInSharedPreference(R.style.passion_theme, R.drawable.passion_gradient);
                break;
            }
            case 9: {
                setBackground(R.drawable.little_leaf_gradient);
                putIntInSharedPreference(R.style.little_leaf_theme, R.drawable.little_leaf_gradient);
                break;
            }
            case 10: {
                setBackground(R.drawable.reef_gradient);
                putIntInSharedPreference(R.style.reef_theme, R.drawable.reef_gradient);
                break;
            }
        }
    }

    private void putIntInSharedPreference(int themeCode, int gradientCode) {
        SharedPrefs.saveTheme(activity, themeCode);
        SharedPrefs.saveBackground(activity, gradientCode);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
