package com.koshka.origami.activites.profile.settings.account;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.activites.profile.settings.account.validators.CurrentPasswordFieldValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 8/29/16.
 */
public class DeleteAccountActivity extends OrigamiActivity {

    private static final String TAG = "DeleteAccountActivity";

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.toolbar_delete_account)
    Toolbar toolbar;

    @BindView(R.id.button_delete_account)
    Button deleteAccountButton;

    @BindView(R.id.current_password_for_delete_layout)
    TextInputLayout currentPasswordLayout;

    @BindView(R.id.current_password_edit_text)
    EditText currentPasswordEditText;

    //----------------------------------------------------------------------------------------------

    private String[] firebaseDbTables;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delete_account);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Resources res = getResources();

        //Init firebase tables;
        firebaseDbTables = res.getStringArray(R.array.user_tables);

    }

    //----------------------------------------------------------------------------------------------

    @OnClick(R.id.button_delete_account)
    public void deleteAccountClicked() {

        //REAUTHENTICATE
        reauthenticate();

        //DELETE AUTH
        deleteAccount();


    }

    //Rauthentication needed before deleting account
    private void reauthenticate() {

        CurrentPasswordFieldValidator validator = new CurrentPasswordFieldValidator(currentPasswordLayout, 6);
        String currentPassword = currentPasswordEditText.getText().toString();
        boolean isCurrentPasswordValid = validator.validate(currentPassword);

        if (isCurrentPasswordValid) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Succesfully reauthenticated");


                    } else {
                        Log.d(TAG, "Couldn't reauthenticate");
                        //Suppose it's because of incorrect password
                        currentPasswordLayout.setError("Password is incorrect");
                    }
                }
            });
        }
    }

    //delete user Auth
    private void deleteAccount() {

        final String userUid = mAuth.getCurrentUser().getUid();
        if (userUid != null || userUid.isEmpty()){

            //DELETE USER DATA BY HIS UID
            deleteUserData(userUid);

            mAuth.getCurrentUser()
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User succesfully deleted" );
                                startActivity(LoginActivity.createIntent(DeleteAccountActivity.this));
                                finish();
                            } else {
                                Log.d(TAG, "Failed to delete account");
                            }
                        }
                    });
        } else {
            //In case uid is null which should never happen
            currentPasswordLayout.setError("Sorry, but something went wrong");
        }
    }

    //loop and delete all user data
    private void deleteUserData(String uid) {
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        String uId = currentUser.getUid();

        if (firebaseDbTables != null) {
            int size = firebaseDbTables.length;
            for (int i = 0; i < size; i++) {
                DatabaseReference mTableRef = mRef.child(firebaseDbTables[i]).child(uId);
                removeValueFromDb(mTableRef);
            }
        }
    }

    //Remove value from every user ref
    private void removeValueFromDb(DatabaseReference reference) {
        if (reference.getKey() != null) {
            reference.removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    //TODO
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            activityHelper.goHome();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}