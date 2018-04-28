package com.firebase.ui.auth.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.R;
import com.firebase.ui.auth.ui.email.RegisterEmailActivity;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.model.User;
import com.koshka.origami.model.Users;

import org.json.JSONException;

/**
 * Created by hackintosh on 3/14/17.
 */

public class FacebookLogInHelper {
    private SignInActivity activity;
    private View view;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private DatabaseReference mDatabase;
    private Users users;
    private AccessToken accessToken;
    private boolean exist = false;

    private String TAG = "FacebookLogIn";
    public static final int RC_REGISTER_ACCOUNT = 14;

    public FacebookLogInHelper(SignInActivity activity) {
        this.activity =  activity;
        //mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //addValueEventListener();
        initializeLogInButton();
    }

    public void initializeLogInButton() {
        // Initialize Facebook Login button
        Log.d("FacebookLogInHelper","initializeButton_start");
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) activity.findViewById(R.id.fb_button);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallbackManager,new FacebookCallback<LoginResult>()

        {
            @Override
            public void onSuccess (LoginResult loginResult){
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                accessToken = loginResult.getAccessToken();
                activity.handleFacebookAccessToken(accessToken);

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

    public void checkInDB(final String email, final String firstName, final String lastName) {
        Log.d("FacebookHelper","email: " + email);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()) {
//                    Log.d("FacebookHelper","emailDB:" + user.child("email").getValue().toString());
//                    Log.d("FacebookHelper","authDB:" + user.child("auth").getValue().toString());
                    if(user.child("email").exists() && user.child("auth").exists()
                            && user.child("email").getValue().toString().equals(email)
                            && user.child("auth").getValue().toString().equals("fb")) {
                        activity.signInFB(accessToken, true);
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    activity.signInFB(accessToken, false);
                    Intent intent = RegisterEmailActivity.createIntent(activity,
                            activity.getmActivityHelper().getFlowParams(),
                            email,"fb",firstName,lastName);
                    activity.startActivityForResult(intent,RC_REGISTER_ACCOUNT);
                }
                //Log.d("FacebookHelper","Users = " + users.getUsersDB().get(0).getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "FacebookHelper:onCancelled", databaseError.toException());
            }
        });
    }

    public CallbackManager getmCallbackManager() { return this.mCallbackManager; }
}
