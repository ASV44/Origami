package com.koshka.origami.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by imuntean on 7/20/16.
 */
public class User {

    private String uid;
    private String email;
    private String nickname;
    private String displayName;
    private String photoUrl;
    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }


    public User(String displayName, String uid, String email) {
        this.displayName = displayName;
        this.uid = uid;
        this.email = email;
    }


    public User(String uid, String email, String nickname, String displayName, String photoUrl) {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String username) {
        this.nickname = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }



}
