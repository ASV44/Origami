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

import com.facebook.login.LoginManager;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.model.Friend;
import com.koshka.origami.model.User;
import com.wang.avi.AVLoadingIndicatorView;

import static android.R.style.Widget;

public class RegisterEmailActivity extends AppCompatBase implements View.OnClickListener {

    private static final int RC_SAVE_CREDENTIAL = 3;
    private static final String TAG = "RegisterEmailActivity";
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EmailFieldValidator mEmailFieldValidator;
    private PasswordFieldValidator mPasswordFieldValidator;
    private UsernameValidator mNameValidator;
    private ImageView mTogglePasswordImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mMeRef;
    private LoginActionBarHelper actionBarHelper;

    private String email;
    private String firstName;
    private String lastName;
    private String auth;
    private boolean userNameExist = true;
    private boolean logOut = true;

    private TextInputLayout mEmailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.create_account_title);
        setContentView(R.layout.register_email_layout);

        String email = getIntent().getStringExtra(ExtraConstants.EXTRA_EMAIL);
        mEmailEditText = (EditText) findViewById(R.id.email);

        auth = getIntent().getStringExtra("auth");
        mMeRef = FirebaseDatabase.getInstance().getReference("users");

        TypedValue visibleIcon = new TypedValue();
        TypedValue slightlyVisibleIcon = new TypedValue();

        getResources().getValue(R.dimen.visible_icon, visibleIcon, true);
        getResources().getValue(R.dimen.slightly_visible_icon, slightlyVisibleIcon, true);

        mPasswordEditText = (EditText) findViewById(R.id.password);
        mTogglePasswordImage = (ImageView) findViewById(R.id.toggle_visibility);
        if(auth.equals("fb")) { mTogglePasswordImage.setVisibility(View.INVISIBLE);}

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

        mFirstNameEditText = (EditText) findViewById(R.id.firs_name);
        mLastNameEditText = (EditText) findViewById(R.id.last_name);

        boolean emailValid;
        if (email != null){
            emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (emailValid) {
                mEmailEditText.setText(email);
                if(auth.equals("fb")) {  mEmailEditText.setEnabled(false); }
                /*mEmailEditText.setEnabled(false);*/
            } else {
                mNameEditText.setText(email);
              // mNameEditText.setEnabled(false);
            }
        }
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        if(firstName != null && lastName != null) {
            mFirstNameEditText.setText(firstName);
            mLastNameEditText.setText(lastName);
        }

        TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        if(auth.equals("fb")) { passwordLayout.setVisibility(View.INVISIBLE); }

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

    @Override
    public void onStop() {
        super.onStop();
        if(logOut) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(logOut) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }
    }

    private void registerUser(final String email, final String username, final String password) {
        final FirebaseAuth firebaseAuth = mActivityHelper.getFirebaseAuth();

        DatabaseReference mUsernameRef = DatabaseRefUtil.getUsersRef();

        final String lowerCaseUserName = username.toLowerCase();

        Query mUsernameQuery = mUsernameRef.orderByChild("username").equalTo(lowerCaseUserName);
        final Resources res = getResources();
        final Uri photoUri = Uri.parse(res.getString(R.string.default_photo_url));
        logOut = false;
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
        //String email = mAuth.getCurrentUser().getEmail();
        String username = mAuth.getCurrentUser().getDisplayName();

        //TAKE FIREBASE REFS

        mMeRef = DatabaseRefUtil.getUserRef(uid);
       /* mOrigamiRef = DatabaseRefUtil.getUserOrigamiRefByUid(uid);*/
        /*mMyFriendsRef= DatabaseRefUtil.getUserFriendsRefByUid(uid);*/
        //-------------------------------------------
        //SETUP THE USER /users/uid

        User user = new User(email, username, firstName, lastName, auth);
        user.setFirstTimeIn(true);
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
        userNameExist = true;
        if (view.getId() == R.id.button_create) {
            String email2 = mEmailEditText.getText().toString();
            String password2 = mPasswordEditText.getText().toString();
            String name2 = mNameEditText.getText().toString();
            firstName = mFirstNameEditText.getText().toString();
            lastName = mLastNameEditText.getText().toString();

            email = email2.trim();
            String password = password2.trim();
            String username = name2.trim();
            firstName = firstName.trim();
            lastName = lastName.trim();

            boolean emailValid = mEmailFieldValidator.validate(email);
            boolean usernameValid = mNameValidator.validate(username);
            boolean passwordValid = mPasswordFieldValidator.validate(password);

            if (emailValid && passwordValid && usernameValid && auth.equals("email")) {
                actionBarHelper.showIndicatorHideTitle();
                registerUser(email, username, password);
            }

            if(auth.equals("fb")) {
                checkUserNameAvailabilityAndRegister(username,password);
            }
        }
    }

    public static Intent createIntent(
            Context context,
            FlowParameters flowParams,
            String email,
            String auth,
            String firstName,
            String lastName) {
        return ActivityHelper.createBaseIntent(context, RegisterEmailActivity.class, flowParams)
                .putExtra(ExtraConstants.EXTRA_EMAIL, email)
                .putExtra("auth",auth)
                .putExtra("firstName",firstName)
                .putExtra("lastName",lastName);
    }

    public void registerUser_FB(final String username, final String password) {
        FirebaseAuth.AuthStateListener mAuthListener;

        final String lower_username = username.toLowerCase();
        final Resources res = getResources();
        final Uri photoUri = Uri.parse(res.getString(R.string.default_photo_url));
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        logOut = false;
        Log.d("Register_FB: USER  ","" + user);
        if(user != null) {
            Log.d("Register_FB:USER_EMAIL ", "" + user.getEmail());
        }
        if(user != null) {
            Task<Void> updateTask = user.updateProfile(
                    new UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(lower_username)
                            .setPhotoUri(photoUri).build());
            updateTask
                    .addOnFailureListener(new TaskFailureLogger(
                            TAG, "Error setting display name"))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            actionBarHelper.hideIndicator();
                            if (task.isSuccessful()) {
                                startSaveCredentials(user, password);
                            }
                        }
                    });
        }

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

    public void checkUserNameAvailabilityAndRegister(final String userName,final String password) {
        //final boolean[] userNameExist = new boolean[1];
        //userNameExist[0] = true;
        Log.d("Register_username",userName);
        mMeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()) {
//                    Log.d("FacebookHelper","username:" + user.child("username").getValue().toString());
//                    Log.d("FacebookHelper","authDB:" + user.child("auth").getValue().toString());
                    if(user.child("username").exists() && user.child("username").getValue()
                            .toString().equals(userName)) {
                        TextInputLayout username_input_layout = (TextInputLayout) findViewById(R.id.name_layout);
                        username_input_layout.setError("Username Already Taken");
                        userNameExist = false;
                        break;
                    }

                }
                if(userNameExist) {
                    actionBarHelper.showIndicatorHideTitle();
                    registerUser_FB(userName, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "FacebookHelper:onCancelled", databaseError.toException());
            }
        });
    }
}
