package com.koshka.origami.helpers.fragment;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.koshka.origami.R;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.adapters.fragment.LoginFragmentPagerAdapter;
import com.koshka.origami.fragments.login.FirstPageFragment3;

/**
 * Created by hackintosh on 3/7/17.
 */

public class FacebookLogInHelper extends AppCompatActivity {
    private LoginActivity activity;
    private Fragment fragment;
    private View view;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;

    private String TAG = "FacebookLogIn";

    public FacebookLogInHelper(Activity activity, Fragment fragment) {
        this.activity = (LoginActivity) activity;
        this.fragment = fragment;
        this.view = fragment.getView();
        //mAuth = FirebaseAuth.getInstance();
        initializeLogInButton();
    }

    public void initializeLogInButton() {
        // Initialize Facebook Login button
        Log.d("FacebookLogInHelper","initializeButton_start");
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.fb_button);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallbackManager,new FacebookCallback<LoginResult>()

        {
            @Override
            public void onSuccess (LoginResult loginResult){
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                activity.handleFacebookAccessToken(loginResult.getAccessToken());
        }

            @Override
            public void onCancel () {
            Log.d(TAG, "facebook:onCancel");
            // ...
        }

            @Override
            public void onError (FacebookException error){
            Log.d(TAG, "facebook:onError", error);
            activity.checkConection();
        }
        });
    }


    public CallbackManager getmCallbackManager() { return this.mCallbackManager; }
}
