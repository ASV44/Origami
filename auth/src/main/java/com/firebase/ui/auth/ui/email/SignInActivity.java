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

package com.firebase.ui.auth.ui.email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.R;
import com.firebase.ui.auth.ui.AcquireEmailHelper;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.firebase.ui.auth.ui.AppCompatBase;
import com.firebase.ui.auth.ui.ExtraConstants;
import com.firebase.ui.auth.ui.FlowParameters;
import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.auth.ui.account_link.SaveCredentialsActivity;
import com.firebase.ui.auth.ui.email.field_validators.EmailFieldValidator;
import com.firebase.ui.auth.ui.email.field_validators.RequiredFieldValidator;
import com.firebase.ui.auth.util.FirebaseAuthWrapperFactory;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.model.User;

import java.util.List;

public class SignInActivity extends AppCompatBase implements View.OnClickListener {
    private static final String TAG = "SignInActivity";
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private RequiredFieldValidator mPasswordValidator;
    private RequiredFieldValidator mLoginNameValidator;
    private ImageView mTogglePasswordImage;
    private DatabaseReference mRef;
    private Query usernameQuery;
    private DatabaseReference mUserRef;
    private AcquireEmailHelper mAcquireEmailHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sign_in);
        setContentView(R.layout.sign_in_layout);

        mAcquireEmailHelper = new AcquireEmailHelper(mActivityHelper);
        String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);

        mEmailEditText = (EditText) findViewById(R.id.email_nickname);

        TypedValue visibleIcon = new TypedValue();
        TypedValue slightlyVisibleIcon = new TypedValue();

        getResources().getValue(R.dimen.visible_icon, visibleIcon, true);
        getResources().getValue(R.dimen.slightly_visible_icon, slightlyVisibleIcon, true);

        mPasswordEditText = (EditText) findViewById(R.id.password);
        mTogglePasswordImage = (ImageView) findViewById(R.id.toggle_visibility);

        mPasswordEditText.setOnFocusChangeListener(new ImageFocusTransparencyChanger(
                mTogglePasswordImage,
                visibleIcon.getFloat(),
                slightlyVisibleIcon.getFloat()));

        mTogglePasswordImage.setOnClickListener(new PasswordToggler(mPasswordEditText));
        mPasswordValidator = new RequiredFieldValidator((TextInputLayout) findViewById(R.id
                .password_layout));

        mLoginNameValidator = new RequiredFieldValidator((TextInputLayout) findViewById(R.id.email_nickname_layout));
        Button signInButton = (Button) findViewById(R.id.button_done);
        TextView recoveryButton =  (TextView) findViewById(R.id.trouble_signing_in);

        if (email != null) {
            mEmailEditText.setText(email);
        }
        signInButton.setOnClickListener(this);
        recoveryButton.setOnClickListener(this);

    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
    }

    private void signIn(String email, final String password) {
        mActivityHelper.getFirebaseAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnFailureListener(
                        new TaskFailureLogger(TAG, "Error signing in with email and password"))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mActivityHelper.dismissDialog();
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (FirebaseAuthWrapperFactory.getFirebaseAuthWrapper(
                                    mActivityHelper.getAppName())
                                    .isPlayServicesAvailable(SignInActivity.this)) {
                                Intent saveCredentialIntent =
                                        SaveCredentialsActivity.createIntent(
                                                SignInActivity.this,
                                                mActivityHelper.getFlowParams(),
                                                firebaseUser.getDisplayName(),
                                                firebaseUser.getEmail(),
                                                password,
                                                null,
                                                null);
                                startActivity(saveCredentialIntent);
                                finish(RESULT_OK, new Intent());
                            }
                        } else {
                            TextInputLayout passwordInput =
                                    (TextInputLayout) findViewById(R.id.password_layout);
                            passwordInput.setError(
                                    getString(com.firebase.ui.auth.R.string.login_error));
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_done) {
            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText()).matches();
            boolean passwordValid = mPasswordValidator.validate(mPasswordEditText.getText());
            boolean loginNameisValid = mLoginNameValidator.validate(mEmailEditText.getText());

            final String loginInput = mEmailEditText.getText().toString();
            if (!passwordValid || !loginNameisValid) {
                return;
            } else if (isEmail && passwordValid) {
                final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
                mActivityHelper.showLoadingDialog(R.string.progress_dialog_loading);
                if (loginInput != null && !loginInput.isEmpty()) {
                    firebaseAuth
                            .fetchProvidersForEmail(loginInput)
                            .addOnFailureListener(
                                    new TaskFailureLogger(TAG, "Error fetching providers for email"))
                            .addOnCompleteListener(
                                    new OnCompleteListener<ProviderQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                            if (task.isSuccessful()) {
                                                List<String> providers = task.getResult().getProviders();
                                                if (providers == null || providers.isEmpty()){
                                                    mAcquireEmailHelper.checkAccountExists(loginInput);
                                                } else {
                                                    for (String provider : providers) {
                                                        if (provider.equalsIgnoreCase(EmailAuthProvider.PROVIDER_ID)) {
                                                            signIn(loginInput, mPasswordEditText.getText().toString());
                                                            return;
                                                        }
                                                    }
                                                }
                                            } else {
                                                mActivityHelper.dismissDialog();
                                            }
                                        }
                                    });

                }
                } else if (!isEmail) {

                    mActivityHelper.showLoadingDialog(R.string.progress_dialog_signing_in);
                    mRef = DatabaseRefUtil.getmUsersRef();
                    mUserRef = DatabaseRefUtil.getmUsersRef();
                    usernameQuery = mRef.orderByChild("nickname").equalTo(loginInput.toLowerCase());
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                mUserRef.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        User user = dataSnapshot.getValue(User.class);
                                        String nickname = user.getNickname().toLowerCase();
                                        String introducedNickname = loginInput.toLowerCase();
                                        if (user.getNickname() != null && nickname.equals(introducedNickname)) {
                                            signIn(user.getEmail(), mPasswordEditText.getText().toString());
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                mAcquireEmailHelper.checkAccountExists(loginInput);
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            } else if (view.getId() == R.id.trouble_signing_in) {
                startActivity(RecoverPasswordActivity.createIntent(
                        this,
                        mActivityHelper.getFlowParams(),
                        mEmailEditText.getText().toString()));
                return;
            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAcquireEmailHelper.onActivityResult(requestCode, resultCode, data);
    }


    public static Intent createIntent(
            Context context,
            FlowParameters flowParams,
            String email) {
        return ActivityHelper.createBaseIntent(context, SignInActivity.class, flowParams)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email);
    }
}
