package com.firebase.ui.database;

import android.content.res.Resources;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.koshka.origami.model.User;

/**
 * Created by imuntean on 7/25/16.
 */
public class DatabaseRefUtil {

    private static final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference getDatabaseReference(){
        return mRef;
    }

    public static DatabaseReference getUsersRef(){
        return mRef.child(String.valueOf(RefConstants.users));
    }

    public static DatabaseReference getPublicOrigamiRef(){
        return mRef.child(String.valueOf(RefConstants.public_origami));
    }

    public static DatabaseReference getFriendsRef(){
        return mRef.child(String.valueOf(RefConstants.friends));
    }

    public static DatabaseReference getPreferencesRef(){
        return mRef.child(String.valueOf(RefConstants.preferences));
    }

    public static DatabaseReference getCurrentLocationRef(){
        return mRef.child(String.valueOf(RefConstants.user_current_location));
    }

    public static DatabaseReference getUserRef(String uid){
        return getUsersRef().child(uid);
    }

    public static DatabaseReference getUserFriendsRef(String uid){
        return getFriendsRef().child(uid);
    }

    public static DatabaseReference getUserPreferencesRef(String uid){
        return getPreferencesRef().child(uid);
    }

    public static DatabaseReference getUserCurrentLocationRef(String uid){
        return getCurrentLocationRef().child(uid);
    }

    public static DatabaseReference isConnectedRef(){
        return getDatabaseReference().child(String.valueOf(RefConstants.connected));
    }


    public static Query getFindUserByEmailQuery(String email) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email);

    }

    public static Query getFindUserByEmailOrNicknameQuery(String email_or_nickname) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email/nickname").equalTo(email_or_nickname);

    }

    public static Query getFindUserByUsernameQuery(String username) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);

    }

}
