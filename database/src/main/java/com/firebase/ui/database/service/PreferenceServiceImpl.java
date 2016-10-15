package com.firebase.ui.database.service;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by qm0937 on 10/7/16.
 */

public class PreferenceServiceImpl implements PreferenceService {

    private FirebaseUser currentUser;

    public PreferenceServiceImpl() {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public void changeTheme(int themeCode) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference backgroundColorRef = database.getReference().child("prefs").child(currentUser.getUid()).child("backgroundColor");

        backgroundColorRef.setValue(themeCode).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    @Override
    public void changeShownName(String shownName) {

    }


}
