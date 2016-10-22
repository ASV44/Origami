package com.koshka.origami.helpers.activity;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by qm0937 on 10/18/16.
 */

public class EmailHelper {

    private ActivityHelper activityHelper;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mRef;

    public EmailHelper(ActivityHelper activityHelper) {
        this.activityHelper = activityHelper;

        mAuth = activityHelper.getFirebaseAuth();
        mUser = activityHelper.getCurrentUser();
    }

    public void checkEmail(final String email){

        boolean emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();


    }

}
