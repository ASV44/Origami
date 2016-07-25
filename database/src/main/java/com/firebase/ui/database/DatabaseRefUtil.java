package com.firebase.ui.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by imuntean on 7/25/16.
 */
public class DatabaseRefUtil {

    private static final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference().child("users");
    private static final DatabaseReference mConnectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");

    public static DatabaseReference getUserRefByUid(String uid){
        return mUsersRef.child(uid);
    }

    public static DatabaseReference getUsersRef(){
        return mUsersRef;
    }

    public static DatabaseReference getmRef() {
        return mRef;
    }

    public static DatabaseReference getmUsersRef() {
        return mUsersRef;
    }

    public static DatabaseReference getmConnectedRef() {
        return mConnectedRef;
    }
}
