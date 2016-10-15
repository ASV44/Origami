package com.firebase.ui.database.service;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.database.DatabaseRefUtil;
import com.firebase.ui.database.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by qm0937 on 10/7/16.
 */

public class UserServiceImpl implements UserService {

    private static final String TAG = "UserServiceImpl";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private String[] firebaseDbTables;

    private  boolean reauthenticationSuccesful = false;

    public UserServiceImpl() {
    currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    public boolean reauthenticate(String email, String password) {

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Reauthenticated succesfully");
                    reauthenticationSuccesful = true;
                } else {
                    Log.d(TAG, "Reauthentication fail");
                    reauthenticationSuccesful = false;
                }
            }
        });
        return reauthenticationSuccesful;
    }


    @Override
    public void deleteFriendsNode() {

    }

    @Override
    public void deleteUserNode() {

    }

    @Override
    public void deletePreferenceNode() {

    }

    @Override
    public boolean deleteAllUserNodes() {

        firebaseDbTables = new String[]{"users", "friends", "prefs", "user_current_location"};
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        String uId = currentUser.getUid();

        if (firebaseDbTables != null) {
            int size = firebaseDbTables.length;
            for (int i = 0; i < size; i++) {
                DatabaseReference mTableRef = mRef.child(firebaseDbTables[i]).child(uId);
                removeValueFromDb(mTableRef);
            }
        }
        return false;
    }

    @Override
    public boolean deleteUser(final Activity activity, final Class activityToGo) {
        firebaseAuth.getCurrentUser()
                .delete()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Failed to delete account");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User succesfully deleted" );
                            activity.startActivity(new Intent(activity, activityToGo));
                            activity.finish();
                        } else {
                            Log.d(activity.getLocalClassName(), "Failed to delete account");
                        }
                    }
                });
        return false;
    }

    @Override
    public void changeEmail(String email) {

    }

    @Override
    public void changeUsername(String username) {

    }

    @Override
    public void firstTimeInTrue() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = DatabaseRefUtil.getUserRef(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstTimeIn");
        ref.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void firstTimeInToFalse() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = DatabaseRefUtil.getUserRef(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstTimeIn");
        ref.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
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
}
