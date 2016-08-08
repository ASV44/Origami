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

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
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
    private List<String> providers = new ArrayList<>();
    private AVLoadingIndicatorView indicatorView;
    public static final int RC_REGISTER_ACCOUNT = 14;
    public static final int RC_SIGN_IN = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sign_in);
        setContentView(R.layout.sign_in_layout);

        mAcquireEmailHelper = new AcquireEmailHelper(mActivityHelper);
        final String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);

        mEmailEditText = (EditText) findViewById(R.id.email_username);

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
        Button registerButton = (Button) findViewById(R.id.button_register);
        TextView recoveryButton = (TextView) findViewById(R.id.trouble_signing_in);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(true);

            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            layout.height = 180;
            layout.width = 180;
            layout.gravity = Gravity.CENTER;
            View view = inflater.inflate(R.layout.av_progress_indicator,null);
            toolbar.addView(view, layout);
        }

        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.av_progress_indicator);
        indicatorView.hide();

        if (email != null) {
            mEmailEditText.setText(email);
        }
        signInButton.setOnClickListener(this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
                final String inputEmail2 = mEmailEditText.getText().toString();
                final String inputEmail = inputEmail2.trim();
                boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches();
                if (!inputEmail.isEmpty()) {
                    if (isEmail) {

                        firebaseAuth.fetchProvidersForEmail(inputEmail)
                                .addOnFailureListener(
                                        new TaskFailureLogger(TAG, "Error fetching providers for email"))
                                .addOnCompleteListener(
                                        new OnCompleteListener<ProviderQueryResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                                if (task.isSuccessful()) {
                                                    List<String> selectedProviders = new ArrayList<>();
                                                    selectedProviders = task.getResult().getProviders();
                                                    if (selectedProviders == null || selectedProviders.isEmpty()) {
                                                        mAcquireEmailHelper.checkAccountExists(inputEmail);
                                                    } else {
                                                        TextInputLayout loginTextLayout = (TextInputLayout) findViewById(R.id.email_nickname_layout);
                                                        loginTextLayout.setError("Email already registered");
                                                        indicatorView.hide();
                                                    }
                                                } else {
                                                    indicatorView.hide();
                                                }
                                            }

                                        });
                    } else {

                        mRef = DatabaseRefUtil.getmUsersRef();
                        mUserRef = DatabaseRefUtil.getmUsersRef();
                        usernameQuery = mRef.orderByChild("username").equalTo(inputEmail);

                        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    indicatorView.show();
                                    TextInputLayout loginTextLayout = (TextInputLayout) findViewById(R.id.email_nickname_layout);
                                    loginTextLayout.setError("Username already registered");
                                    indicatorView.hide();
                                } else {

                                    indicatorView.show();

                                        ArrayList<String> selectedProviders = new ArrayList<>();
                                        startEmailHandler(inputEmail, selectedProviders);
                                    }
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                } else if(!isEmail) {
                    ArrayList<String> selectedProviders = new ArrayList<>();
                    startEmailHandler(email, selectedProviders);


                } else {
                    mAcquireEmailHelper.checkAccountExists(inputEmail);
                }

            }
        });
        recoveryButton.setOnClickListener(this);

    }

    private void startEmailHandler(String email, List<String> providers) {
        indicatorView.hide();
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

    @Override
    public void onBackPressed() {
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
                        indicatorView.hide();
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
        hideKeyboard();
        if (view.getId() == R.id.button_done) {
            boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText()).matches();
            boolean passwordValid = mPasswordValidator.validate(mPasswordEditText.getText());
            boolean loginNameisValid = mLoginNameValidator.validate(mEmailEditText.getText());

            final String loginInput2 = mEmailEditText.getText().toString();
            final String loginInput = loginInput2.trim();

            if (loginInput.contains(" ")){

                TextInputLayout loginTextLayout = (TextInputLayout) findViewById(R.id.email_nickname_layout);
                loginTextLayout.setError("WTF?");
                mActivityHelper.dismissDialog();
                return;

            }
            if (!passwordValid || !loginNameisValid) {
                return;
            } else if (isEmail && passwordValid) {
                final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();
                indicatorView.show();
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
                                                if (providers == null || providers.isEmpty()) {
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
                                                indicatorView.hide();
                                            }
                                        }
                                    });

                }
            } else if (!isEmail) {

                indicatorView.show();
                mRef = DatabaseRefUtil.getmUsersRef();
                mUserRef = DatabaseRefUtil.getmUsersRef();
                usernameQuery = mRef.orderByChild("username").equalTo(loginInput.toLowerCase());
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mUserRef.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    User user = dataSnapshot.getValue(User.class);
                                    String nickname = user.getUsername().toLowerCase();
                                    String introducedNickname = loginInput.toLowerCase();
                                    if (user.getUsername() != null && nickname.equals(introducedNickname)) {
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
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
