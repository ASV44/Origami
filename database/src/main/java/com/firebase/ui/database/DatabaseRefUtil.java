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
    private static final DatabaseReference mMyFriendsRef = null;
    private static final DatabaseReference mMyFriendRequestsRef = null;
    private static final DatabaseReference mNickname = null;

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

    public static Query getFindUserByEmailQuery(String email) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email").equalTo(email);

    }

    public static Query getFindUserByEmailOrNicknameQuery(String email_or_nickname) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("email/nickname").equalTo(email_or_nickname);

    }

    public static Query getFindUserByNicknameQuery(String nickname) {
        return FirebaseDatabase.getInstance().getReference().child("users").orderByChild("nickname").equalTo(nickname);

    }

    public static Query getFindUserByEmailQuery() {
        return findUserByEmailQuery;
    }

    public static DatabaseReference getmMyFriendsRef(String myUid) {
        return mUsersRef.child(myUid).child("friendList");
    }

    public static DatabaseReference getmMyFriendRequestsRef() {
        return mMyFriendRequestsRef;
    }

    public static DatabaseReference getmNickname() {
        return mUsersRef.child("nickname");
    }
}
