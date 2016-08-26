package com.firebase.ui.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.koshka.origami.model.User;

/**
 * Created by imuntean on 7/25/16.
 */
public class DatabaseRefUtil {

    private static final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private static final DatabaseReference mUsersRef = FirebaseDatabase.getInstance().getReference().child("users");
    private static final DatabaseReference mConnectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");
    private static final Query findUserByEmailQuery = null;
    private static final DatabaseReference mFriendsRef = FirebaseDatabase.getInstance().getReference().child("friends");
    private static final DatabaseReference mOrigamiRef = FirebaseDatabase.getInstance().getReference().child("origami");
    public static DatabaseReference getUserRefByUid(String uid){
        return mUsersRef.child(uid);
    }
    public static DatabaseReference getUserFriendsRefByUid(String uid){
        return mFriendsRef.child(uid);
    }
    public static DatabaseReference getUserOrigamiRefByUid(String uid){
        return mOrigamiRef.child(uid);
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

    public static Query getFindUserByEmailQuery(String email) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email);

    }

    public static Query getFindUserByEmailOrNicknameQuery(String email_or_nickname) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email/nickname").equalTo(email_or_nickname);

    }

    public static Query getFindUserByUsernameQuery(String username) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(username);

    }

    public static Query getFindUserByEmailQuery() {
        return findUserByEmailQuery;
    }

    public static DatabaseReference getmMyFriendsRef(String myUid) {
        return mFriendsRef.child(myUid);
    }

    public static DatabaseReference getmFriendsRef() {
        return mFriendsRef;
    }
}
