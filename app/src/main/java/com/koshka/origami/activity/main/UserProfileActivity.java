package com.koshka.origami.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.koshka.origami.R;
import com.koshka.origami.fragment.profile.UserProfileFragmentPagerAdapter;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/19/16.
 */
public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    public static final String SHARED_PREFS = "SharedPrefs";

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    @BindView(R.id.profile_pager)
    ViewPager mPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int theme = prefs.getInt("theme", -1);

        setTheme(theme);

        setContentView(R.layout.user_profile_layout);
        ButterKnife.bind(this);



        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));
        pt.setBorder(0);
        pt.setSpeed(0.7f);

        mPager.setPageTransformer(false, pt);
        mPager.setAdapter(new UserProfileFragmentPagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);

        viewpagertab.setViewPager(mPager);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, UserProfileActivity.class);
        return in;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_view:
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

}
