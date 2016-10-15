package com.koshka.origami.activity;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.factory.FragmentPagerAdapterFactory;
import com.koshka.origami.utils.SharedPrefs;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

/**
 * Created by qm0937 on 10/15/16.
 */

public abstract class GenericOrigamiActivity extends AppCompatActivity {

    @BindView(android.R.id.content)
    protected View mRootView;

    protected static FirebaseUser currentUser;
    protected static FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseInit();

        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        SharedPrefs.changeTheme(this);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
        }

        serviceSetup();
    }

    private void firebaseInit(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
    }

    //---------------------------------------------------------------------------------------------
    protected abstract void serviceSetup();
    protected abstract void uiSetup();

    //---------------------------------------------------------------------------------------------

    //Util method for building a pager adapter using the factory
    protected FragmentPagerAdapter buildPagerAdapter(String pagerAdapter){
        return FragmentPagerAdapterFactory.build(pagerAdapter, getSupportFragmentManager());
    }

    //Just a faster way to set view pager and smart tab layout
    protected void fragmentSetup(ViewPager viewPager, String adapter, int currentFragment, SmartTabLayout smartTabLayout) {

        viewPager.setAdapter(buildPagerAdapter(adapter));
        viewPager.setCurrentItem(currentFragment);

        smartTabLayout.setViewPager(viewPager);

    }


    @MainThread
    protected void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



}
