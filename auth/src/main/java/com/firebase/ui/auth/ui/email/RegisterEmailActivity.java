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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.R;
import com.firebase.ui.auth.ui.ActivityHelper;
import com.firebase.ui.auth.ui.AppCompatBase;
import com.firebase.ui.auth.ui.ExtraConstants;
import com.firebase.ui.auth.ui.FlowParameters;
import com.firebase.ui.auth.ui.TaskFailureLogger;
import com.firebase.ui.auth.ui.account_link.SaveCredentialsActivity;
import com.firebase.ui.auth.ui.email.field_validators.EmailFieldValidator;
import com.firebase.ui.auth.ui.email.field_validators.UsernameValidator;
import com.firebase.ui.auth.ui.email.field_validators.PasswordFieldValidator;
import com.firebase.ui.auth.util.FirebaseAuthWrapperFactory;
import com.firebase.ui.auth.util.LoginActionBarHelper;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.model.Friend;
import com.koshka.origami.model.User;
import com.wang.avi.AVLoadingIndicatorView;

public class RegisterEmailActivity extends AppCompatBase implements View.OnClickListener {

    private static final int RC_SAVE_CREDENTIAL = 3;
    private static final String TAG = "RegisterEmailActivity";
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;
    private EmailFieldValidator mEmailFieldValidator;
    private PasswordFieldValidator mPasswordFieldValidator;
    private UsernameValidator mNameValidator;
    private ImageView mTogglePasswordImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mMeRef;
    private LoginActionBarHelper actionBarHelper;

    private TextInputLayout mEmailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.create_account_title);
        setContentView(R.layout.register_email_layout);

        String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);
        mEmailEditText = (EditText) findViewById(R.id.email);

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

        mNameEditText = (EditText) findViewById(R.id.name);

        mPasswordFieldValidator = new PasswordFieldValidator((TextInputLayout)
                findViewById(R.id.password_layout),
                getResources().getInteger(R.integer.min_password_length));
        mNameValidator = new UsernameValidator((TextInputLayout)
                findViewById(R.id.name_layout));
        mEmailFieldValidator = new EmailFieldValidator((TextInputLayout) findViewById(R.id
                .email_layout));

        boolean emailValid;
        if (email != null){
            emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (emailValid) {
                mEmailEditText.setText(email);
                /*mEmailEditText.setEnabled(false);*/
            } else {
                mNameEditText.setText(email);
              // mNameEditText.setEnabled(false);
            }
        }


        actionBarHelper = new LoginActionBarHelper(this, R.id.toolbar_sign_up);
        actionBarHelper.buildTitlePlusIndicatorActionBar("Register...");


        setUpTermsOfService();
        Button createButton = (Button) findViewById(R.id.button_create);
        createButton.setOnClickListener(this);
        setupTheScrollView();
    }

    private void setupTheScrollView(){
        final ScrollView scrollView = (ScrollView) findViewById(R.id.register_scroll_view);

        final Context context = getApplicationContext();

        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    scrollView.smoothScrollTo(0, mNameEditText.getTop());
                }
            }
        });

    }

    private void setUpTermsOfService() {
        if (mActivityHelper.getFlowParams().termsOfServiceUrl == null) {
            return;
        }
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor
                (getApplicationContext(), R.color.linkColor));

        String preamble = getResources().getString(R.string.create_account_preamble);
        String link = getResources().getString(R.string.terms_of_service);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(preamble + link);
        int start = preamble.length();
        spannableStringBuilder.setSpan(foregroundColorSpan, start, start + link.length(), 0);
        TextView agreementText = (TextView) findViewById(R.id.create_account_text);
        agreementText.setText(spannableStringBuilder);
        agreementText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse
                        (mActivityHelper.getFlowParams().termsOfServiceUrl));
                startActivity(intent);
            }
        });
    }

    private void startSaveCredentials(FirebaseUser firebaseUser, String password) {
        if (FirebaseAuthWrapperFactory.getFirebaseAuthWrapper(mActivityHelper.getFlowParams().appName)
                .isPlayServicesAvailable(this)) {
            Intent saveCredentialIntent = SaveCredentialsActivity.createIntent(
                    this,
                    mActivityHelper.getFlowParams(),
                    firebaseUser.getDisplayName(),
                    firebaseUser.getEmail(),
                    password,
                    null,
                    null);
            startActivityForResult(saveCredentialIntent, RC_SAVE_CREDENTIAL);
        }
    }

    private void registerUser(final String email, final String username, final String password) {
        final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();

        DatabaseReference mUsernameRef = DatabaseRefUtil.getUsersRef();

        final String lowerCaseUserName = username.toLowerCase();

        Query mUsernameQuery = mUsernameRef.orderByChild("username").equalTo(lowerCaseUserName);
        final Resources res = getResources();
        final Uri photoUri = Uri.parse(res.getString(R.string.default_photo_url));
        mUsernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnFailureListener(new TaskFailureLogger(TAG, "Error creating user"))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        final FirebaseUser firebaseUser = task.getResult().getUser();
                                        Task<Void> updateTask = firebaseUser.updateProfile(
                                                new UserProfileChangeRequest
                                                        .Builder()
                                                        .setDisplayName(lowerCaseUserName)
                                                        .setPhotoUri(photoUri).build());
                                        updateTask
                                                .addOnFailureListener(new TaskFailureLogger(
                                                        TAG, "Error setting display name"))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        actionBarHelper.hideIndicator();
                                                        if (task.isSuccessful()) {
                                                            startSaveCredentials(firebaseUser, password);
                                                        }
                                                    }
                                                });
                                    } else {
                                        actionBarHelper.hideIndicator();
                                        String errorMessage = task.getException().getLocalizedMessage();
                                        errorMessage = errorMessage.substring(errorMessage.indexOf(":") + 1);
                                        TextInputLayout emailInput =
                                                (TextInputLayout) findViewById(R.id.email_layout);
                                        emailInput.setError(errorMessage);
                                    }
                                }
                            });
                }else {

                    actionBarHelper.hideIndicator();
                    TextInputLayout nameLayout =
                            (TextInputLayout) findViewById(R.id.name_layout);
                    nameLayout.setError("This username is taken");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SAVE_CREDENTIAL) {
            initUserInDatabase();
            finish(RESULT_OK, new Intent());
        }
    }
    //Initialization of user in firebase database
    //Using setValue because it's only once
    private void initUserInDatabase(){
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        String email = mAuth.getCurrentUser().getEmail();
        String username = mAuth.getCurrentUser().getDisplayName();

        //TAKE FIREBASE REFS

        mMeRef = DatabaseRefUtil.getUserRef(uid);
       /* mOrigamiRef = DatabaseRefUtil.getUserOrigamiRefByUid(uid);*/
        /*mMyFriendsRef= DatabaseRefUtil.getUserFriendsRefByUid(uid);*/
        //-------------------------------------------
        //SETUP THE USER /users/uid

        User user = new User(email, username);
        mMeRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e(TAG, "Failed to store user to db", databaseError.toException());
                }
            }
        });

        //SETUP FIRST FRIEND /friends/uid
        User ghostFriend = new User();

        ghostFriend.setDisplayName("Ghost");
        ghostFriend.setUsername("Ghost");
        ghostFriend.setEmail("ghost@origami.com");

        DatabaseReference friendsRef = DatabaseRefUtil.getUserFriendsRef(uid);

        friendsRef.push().setValue(ghostFriend, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e(TAG, "Failed to store friend to db", databaseError.toException());
                }
            }
        });

        //-------------------------------------------
        //WE DON"T NEED THIS
      /*  //SETUP FIRST ORIGAMI /origami/uid
        GhostOrigami origami = new GhostOrigami();

        origami.setText(res.getString(R.string.origami_text));
        origami.setOrigamiName(res.getString(R.string.origami_name));
        mOrigamiRef.push().setValue(origami, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e(TAG, "Failed to store origami to db", databaseError.toException());
                }
            }
        });
        //-------------------------------------------
        //SETUP FIRST FRIEND /friends/uid
        Friend ghostFriend = new Friend();

        ghostFriend.setDisplayName("Ghost");
        ghostFriend.setEmail("ghost@origami.com");
        ghostFriend.setNickname("OrigamiGhost");

        mMyFriendsRef.push().setValue(ghostFriend, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e(TAG, "Failed to store friend to db", databaseError.toException());
                }
            }
        });*/
        //-------------------------------------------

    }

    @Override
    public void onClick(View view) {
        hideKeyboard(this);
        if (view.getId() == R.id.button_create) {
            String email2 = mEmailEditText.getText().toString();
            String password2 = mPasswordEditText.getText().toString();
            String name2 = mNameEditText.getText().toString();

            String email = email2.trim();
            String password = password2.trim();
            String username = name2.trim();

            boolean emailValid = mEmailFieldValidator.validate(email);
            boolean usernameValid = mNameValidator.validate(username);
            boolean passwordValid = mPasswordFieldValidator.validate(password);

            if (emailValid && passwordValid && usernameValid) {
                actionBarHelper.showIndicatorHideTitle();
                registerUser(email, username, password);
            }
        }
    }

    public static Intent createIntent(
            Context context,
            FlowParameters flowParams,
            String email) {
        return ActivityHelper.createBaseIntent(context, RegisterEmailActivity.class, flowParams)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
