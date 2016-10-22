package com.koshka.origami.helpers.activity;


import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.koshka.origami.R;
import com.koshka.origami.activites.main.OrigamiMapActivity;
import com.koshka.origami.activites.profile.UserProfileActivity;
import com.koshka.origami.adapters.fragment.FragmentAdapters;
import com.koshka.origami.helpers.OrigamiActionBarHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by qm0937 on 10/19/16.
 */

public class MainActivityHelper {

    private AppCompatActivity activity;
    private FragmentActivityHelper activityHelper;

    private static final int FIRST_LOGIN_PREFS_REQUEST = 11;

    private static final String adapter = FragmentAdapters.MAIN;

    public MainActivityHelper(AppCompatActivity activity) {
        this.activity = activity;
        activityHelper = new FragmentActivityHelper(activity);


    }

    public void toolbarSetup(Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        OrigamiActionBarHelper.setMainActivityAttributes(activity, activity.getSupportActionBar());
    }

    public void fragmentSetup(ViewPager mPager, SmartTabLayout mSmartTab) {
        activityHelper.fragmentSetup(mPager, adapter, 1, mSmartTab);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FIRST_LOGIN_PREFS_REQUEST: {
                // SharedPrefs.changeBackground(this, mRootView);
                break;
            }
        }


    }

    public void buildMenu(Menu menu) {
        activity.getMenuInflater().inflate(R.menu.menu, menu);
    }

    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                activity.startActivity(new Intent(activity, UserProfileActivity.class));
                break;
            case R.id.map_view:
                activity.startActivity(new Intent(activity, OrigamiMapActivity.class));
                break;

        }
    }
}
