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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
