package com.koshka.origami.activites.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;
import com.koshka.origami.helpers.activity.MainActivityHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends OrigamiActivity {

    private final static String TAG = "MainActivity";

    //---------------------------------------------------------------------------------------------

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout mSmartTab;

    //----------------------------------------------------------------------------------------------

    private MainActivityHelper mainActivityHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        super.activityHelper.checkFirebase(this);

        mainActivityHelper = new MainActivityHelper(this);

        mainActivityHelper.fragmentSetup(mPager, mSmartTab);
        mainActivityHelper.toolbarSetup(mToolbar);


    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainActivityHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivityHelper.buildMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mainActivityHelper.onOptionsItemSelected(item);
        return true;
    }

    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

}


