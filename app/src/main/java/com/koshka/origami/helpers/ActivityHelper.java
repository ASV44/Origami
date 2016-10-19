package com.koshka.origami.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.utils.SharedPrefs;
import com.koshka.origami.utils.net.NetworkUtil;
import com.koshka.origami.utils.ui.UiNavigationUtil;

/**
 * Created by qm0937 on 10/18/16.
 */

public class ActivityHelper {

    private Activity mActivity;

    public ActivityHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void configureTheme() {
        SharedPrefs.changeTheme(mActivity);
    }

    public Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

    public FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public void goHome(){
        UiNavigationUtil.goHome(mActivity);
    }

    public boolean isNetworkOn(){
        return NetworkUtil.isNetworkConnected(mActivity);
    }

    public Resources getRes(){
        return mActivity.getResources();
    }


    public void checkFirebase(Activity activity){
        if (getCurrentUser() == null) {
            activity.startActivity(LoginActivity.createIntent(activity));
            activity.finish();
            return;
        }
    }

    public Activity getmActivity() {
        return mActivity;
    }
}
