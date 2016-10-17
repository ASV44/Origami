package com.koshka.origami.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.activity.profile.UserProfileActivity;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.helpers.OrigamiActionBarHelper;
import com.koshka.origami.helpers.TypeFaceHelper;
import com.koshka.origami.utils.SharedPrefs;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends GenericOrigamiActivity {

    private final static String TAG = "MainActivity";

    private static final int FIRST_LOGIN_PREFS_REQUEST = 11;

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout mSmartTab;

    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;

    @BindView(R.id.text_origami_logo)
    TextView origamiLogoTextView;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkFirebase(this);
        serviceSetup();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TypeFaceHelper.setTypeFace(this, origamiLogoTextView, TypeFaceHelper.origamiLogoTypeFace);

        setSupportActionBar(mToolbar);

        OrigamiActionBarHelper.setMainActivityAttributes(this, getSupportActionBar());

        fragmentSetup(mPager, FragmentAdapters.MAIN, 1, mSmartTab);

    }

    //----------------------------------------------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FIRST_LOGIN_PREFS_REQUEST: {
                SharedPrefs.changeBackground(this, mRootView);
                break;
            }

        }
    }

    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.map_view:
                startActivity(new Intent(this, OrigamiMapActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}


