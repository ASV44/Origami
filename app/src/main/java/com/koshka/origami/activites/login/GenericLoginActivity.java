package com.koshka.origami.activites.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StyleRes;

import com.firebase.ui.auth.AuthUI;
import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

import java.util.ArrayList;

/**
 * Created by qm0937 on 10/17/16.
 */

public class GenericLoginActivity extends OrigamiActivity {

    private static final String TAG = "GenericLoginActivity";

    private static final String GOOGLE_TOS_URL = "https://www.google.com/policies/terms/";

    //----------------------------------------------------------------------------------------------

    private OrigamiThemeHelper origamiThemeHelper;
    private int backButtonCount;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        origamiThemeHelper = new OrigamiThemeHelper(this);
        origamiThemeHelper.randomThemeSetAndSave();
    }

    //----------------------------------------------------------------------------------------------

    @MainThread
    @StyleRes
    protected int getSelectedTheme() {

        if (origamiThemeHelper.getRandomPickedTheme() != -1) {
            return origamiThemeHelper.getRandomPickedTheme();
        } else {
            return R.style.amethist_theme;
        }

    }

    @MainThread
    @DrawableRes
    protected int getSelectedLogo() {
        return AuthUI.NO_LOGO;
    }

    @MainThread
    protected String[] getSelectedProviders() {

        ArrayList<String> selectedProviders = new ArrayList<>();
        selectedProviders.add(AuthUI.EMAIL_PROVIDER);
        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @MainThread
    protected String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
    }


    //----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {

        //TODO: ON DOUBLE BACK PRESSED ISN't WORKING PROPERLY, FIND ANOTHER SOLUTION...
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            showShortSnackbar(R.string.press_back);
            backButtonCount++;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
