package com.koshka.origami.helpers.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.activites.login.GenericLoginActivity;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.utils.SharedPrefs;
import com.koshka.origami.utils.net.NetworkUtil;
import com.koshka.origami.utils.ui.UiNavigationUtil;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

/**
 * Created by qm0937 on 10/18/16.
 */

public class ActivityHelper {

    private Activity mActivity;
    private OrigamiThemeHelper themeHelper;

    public ActivityHelper(Activity mActivity) { this.mActivity = mActivity; }

    public void configureTheme() {
        if(mActivity.getIntent().getExtras().getInt("theme") != 0) {
            //SharedPrefs.changeTheme(mActivity,mActivity.getIntent().getExtras().getInt("theme"));
            themeHelper = new OrigamiThemeHelper(mActivity,
                    mActivity.getIntent().getExtras().getInt("theme"));
        }
        else {
            themeHelper = new OrigamiThemeHelper(mActivity);
        }
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


//    public void checkFirebase(Activity activity){
//        if (getCurrentUser() == null) {
//            activity.startActivity(LoginActivity.createIntent(activity));
//            activity.finish();
//        }
//    }

    public Activity getmActivity() {
        return mActivity;
    }

    public OrigamiThemeHelper getThemeHelper() { return this.themeHelper; }
}
