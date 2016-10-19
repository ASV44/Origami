package com.koshka.origami.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.activity.profile.settings.account.ChangePasswordActivity;

/**
 * Created by qm0937 on 10/18/16.
 */

public class PasswordHelper {

    private final static String TAG = "PasswordHelper";

    private ActivityHelper activityHelper;

    private static FirebaseUser currentUser;
    private static FirebaseAuth mAuth;

    private static boolean passOK = false;

    public PasswordHelper(ActivityHelper activityHelper) {
        this.activityHelper = activityHelper;

        currentUser = activityHelper.getCurrentUser();
        mAuth = activityHelper.getFirebaseAuth();
    }


    public void reauthenticate(final String password) {

        AuthCredential credential = EmailAuthProvider.getCredential(activityHelper.getCurrentUser().getEmail(), password);

        currentUser.reauthenticate(credential).addOnCompleteListener(activityHelper.getmActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    task.getResult();
                    startPasswordHandler(true);
                } catch (Exception e){
                    Log.d(TAG, e.getMessage());
                    startPasswordHandler(false);
                }


            }
        });
    }

    public void startPasswordHandler(boolean isCorrect) {

            if (isCorrect) {
                Log.d(TAG, "Email was updated." + activityHelper.getmActivity().toString());


            } else {
                Log.d(TAG, "password incorrect.");
                Log.d(TAG, "Email was updated." + activityHelper.getmActivity().toString());
            }

    }

    public void changePassword() {


    }
}
