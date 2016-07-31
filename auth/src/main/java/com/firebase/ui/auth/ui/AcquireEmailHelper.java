/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebase.ui.auth.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Patterns;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.R;
import com.firebase.ui.auth.ui.account_link.WelcomeBackIDPPrompt;
import com.firebase.ui.auth.ui.email.RegisterEmailActivity;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.firebase.ui.auth.util.ProviderHelper;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AcquireEmailHelper {
    private static final String TAG = "AcquireEmailHelper";
    public static final int RC_REGISTER_ACCOUNT = 14;
    public static final int RC_WELCOME_BACK_IDP = 15;
    public static final int RC_SIGN_IN = 16;
    public static final List<Integer> REQUEST_CODES = Arrays.asList(
            RC_REGISTER_ACCOUNT,
            RC_WELCOME_BACK_IDP,
            RC_SIGN_IN
    );

    private ActivityHelper mActivityHelper;

    private DatabaseReference mRef;
    private Query usernameQuery;
    private DatabaseReference mUserRef;

    public AcquireEmailHelper(ActivityHelper activityHelper) {
        mActivityHelper = activityHelper;
    }

    public void checkAccountExists(final String email) {
        boolean emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
        if (emailValid) {

            mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
            if (email != null && !email.isEmpty()) {
                firebaseAuth
                        .fetchProvidersForEmail(email)
                        .addOnFailureListener(
                                new TaskFailureLogger(TAG, "Error fetching providers for email"))
                        .addOnCompleteListener(
                                new OnCompleteListener<ProviderQueryResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                        if (task.isSuccessful()) {
                                            startEmailHandler(email, task.getResult().getProviders());
                                        } else {
                                            mActivityHelper.dismissDialog();
                                        }
                                    }
                                });
            }
        } else {

            mRef = DatabaseRefUtil.getmUsersRef();
            mUserRef = DatabaseRefUtil.getmUsersRef();
            usernameQuery = mRef.orderByChild("nickname").equalTo(email);

            usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
                        ArrayList<String> selectedProviders = new ArrayList<>();
                        selectedProviders.add(EmailAuthProvider.PROVIDER_ID);
                        startEmailHandler(email, selectedProviders);
                    } else {

                        mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
                        if (email != null && !email.isEmpty()) {

                                                        ArrayList<String> selectedProviders = new ArrayList<>();
                                                        startEmailHandler(email, selectedProviders);

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    private void startEmailHandler(String email, List<String> providers) {
        mActivityHelper.dismissDialog();
        if (providers == null || providers.isEmpty()) {
            // account doesn't exist yet
            Intent registerIntent = RegisterEmailActivity.createIntent(
                    mActivityHelper.getApplicationContext(),
                    mActivityHelper.getFlowParams(),
                    email);
            mActivityHelper.startActivityForResult(registerIntent, RC_REGISTER_ACCOUNT);
            return;
        } else {
            // account does exist
            for (String provider : providers) {
                if (provider.equalsIgnoreCase(EmailAuthProvider.PROVIDER_ID)) {
                    Intent signInIntent = SignInActivity.createIntent(
                            mActivityHelper.getApplicationContext(),
                            mActivityHelper.getFlowParams(),
                            email);
                    mActivityHelper.startActivityForResult(signInIntent, RC_SIGN_IN);
                    return;
                }
            }

            Intent signInIntent = new Intent(
                    mActivityHelper.getApplicationContext(), SignInActivity.class);
            signInIntent.putExtra(ExtraConstants.EXTRA_EMAIL, email);
            mActivityHelper.startActivityForResult(signInIntent, RC_SIGN_IN);
            return;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODES.contains(requestCode)) {
            mActivityHelper.finish(resultCode, new Intent());
        }
    }
}
