package com.koshka.origami.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/19/16.
 */
public class UserProfileActivity extends AppCompatActivity implements ValueEventListener {

    private static final String TAG = "UserProfileActivity";

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;

    @BindView(R.id.user_email)
    TextView mUserEmail;

    @BindView(R.id.isConnected)
    TextView isConnected;

    @BindView(R.id.user_display_name)
    TextView mUserDisplayName;

    private DatabaseReference mMeRef;
    private FirebaseAuth mAuth;

    private DatabaseReference connectedRef;
    private ValueEventListener isConnectedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mMeRef = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.user_profile_layout);
        ButterKnife.bind(this);
        mMeRef.addValueEventListener(this);
        populateProfile();
    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(UserProfileActivity.this));
                            finish();
                        } else {
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    @OnClick(R.id.delete_account)
    public void deleteAccountClicked() {

        Resources res = getResources();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(res.getString(R.string.delete_warning))
                .setPositiveButton(res.getString(R.string.positive_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccountFromDb();
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .create();

        dialog.show();
    }

    private void deleteAccountFromDb(){

        String user = mMeRef.getKey();
        if (user != null){
                mMeRef.removeValue();

        }
        mMeRef.removeEventListener(this);
        connectedRef.removeEventListener(isConnectedListener);
    }


    private void deleteAccount() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(LoginActivity.createIntent(UserProfileActivity.this));
                            finish();
                        } else {
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }

    @MainThread
    private void populateProfile() {

        connectedRef = DatabaseRefUtil.getmConnectedRef();
        isConnectedListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    isConnected.setText("connected");
                } else {
                    isConnected.setText("disconnected");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Listener was cancelled");
            }
        };

        connectedRef.addValueEventListener(isConnectedListener);

    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, UserProfileActivity.class);
        return in;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (dataSnapshot.getValue() != null){
            User user = dataSnapshot.getValue(User.class);
            mUserEmail.setText(user.getEmail());
            mUserDisplayName.setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                Glide.with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .fitCenter()
                        .into(mUserProfilePicture);
            }
        } else {
            showSnackbar(R.string.something_went_wrong);
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
