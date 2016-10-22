package com.koshka.origami.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.ui.ActivityHelper;

/**
 * Created by qm0937 on 10/19/16.
 */

public class ActivityBase extends Activity {

    protected ActivityHelper mActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mActivityHelper = new ActivityHelper(this, getIntent());
        mActivityHelper.configureTheme();
    }

    public void finish(int resultCode, Intent intent) {
        mActivityHelper.finish(resultCode, intent);
    }
}
