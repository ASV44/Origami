package com.koshka.origami.utils.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;

/**
 * Created by imuntean on 8/29/16.
 */
public class UiNavigationUtil {

    public static void goHome(Activity activity){
        Intent intent = NavUtils.getParentActivityIntent(activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NavUtils.navigateUpTo(activity, intent);
    }
}
