package com.koshka.origami.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.helpers.ActivityHelper;

import butterknife.BindView;

/**
 * Created by qm0937 on 10/19/16.
 */

public class AppCompatBase extends AppCompatActivity {


    @BindView(android.R.id.content)
    protected View mRootView;

    protected ActivityHelper activityHelper;

    protected static FirebaseAuth mAuth;
    protected static FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        activityHelper = new ActivityHelper(this);
        activityHelper.configureTheme();

        mAuth = activityHelper.getFirebaseAuth();
        currentUser = activityHelper.getCurrentUser();
    }


    @MainThread
    protected void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @MainThread
    protected void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }


}
