package com.koshka.origami.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.factory.FragmentPagerAdapterFactory;
import com.koshka.origami.utils.SharedPrefs;
import com.koshka.origami.utils.net.NetworkUtil;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

/**
 * Created by qm0937 on 10/15/16.
 */

public abstract class GenericOrigamiActivity extends AppCompatActivity {

    private static final String TAG = "GenericActivity";

    private static boolean networkOn = false;

    //----------------------------------------------------------------------------------------------

    @BindView(android.R.id.content)
    protected View mRootView;

    //----------------------------------------------------------------------------------------------

    protected static FirebaseUser currentUser;
    protected static FirebaseAuth mAuth;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseInit();
        SharedPrefs.changeTheme(this);


        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    //----------------------------------------------------------------------------------------------
    private void firebaseInit(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
    }

    //----------------------------------------------------------------------------------------------

    protected void serviceSetup(){

    };

    protected void uiSetup(){

    };

    //---------------------------------------------------------------------------------------------

    //Util method for building a pager adapter using the factory
    private FragmentPagerAdapter buildPagerAdapter(String pagerAdapter){
        return FragmentPagerAdapterFactory.build(pagerAdapter, getSupportFragmentManager());
    }

    //Just a faster way to set view pager and smart tab layout
    protected void fragmentSetup(ViewPager viewPager, String adapter, int currentFragment, SmartTabLayout smartTabLayout) {

        viewPager.setAdapter(buildPagerAdapter(adapter));
        viewPager.setCurrentItem(currentFragment);


        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.recycler_view));
        viewPager.setPageTransformer(false, pt);

        if (smartTabLayout != null){
            smartTabLayout.setViewPager(viewPager);
        }


    }

    protected void checkFirebase(Activity activity){
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(activity));
            activity.finish();
            return;
        }
    }

    //util method for intent creation from other activities
    public static Intent createIntent(Context context, Class activity) {
        Intent in = new Intent();
        in.setClass(context, activity);
        return in;
    }



    @MainThread
    protected void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @MainThread
    protected void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected boolean isNetworkOn(){
        return NetworkUtil.isNetworkConnected(this);
    }

}
