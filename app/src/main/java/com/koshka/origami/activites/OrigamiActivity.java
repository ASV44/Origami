package com.koshka.origami.activites;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.data.network.api.RetrofitAPI;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.activites.main.MainActivity;
import com.koshka.origami.helpers.activity.ActivityHelper;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

import butterknife.BindView;

/**
 * Created by qm0937 on 10/19/16.
 */

public class OrigamiActivity extends AppCompatActivity {


    @BindView(android.R.id.content)
    protected View mRootView;

    protected ActivityHelper activityHelper;

    protected static FirebaseAuth mAuth;
    protected static FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 100;

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

    public ActivityHelper getActivityHelper() { return  this.activityHelper; }

    public void checkFirebase(MainActivity activity) {
        if (activityHelper.getCurrentUser() == null) {
            activity.startActivity(LoginActivity.createIntent(activity));
            activity.finish();
            return;
        }
    }
}
