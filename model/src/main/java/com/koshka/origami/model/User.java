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
    private String username;
    private String displayName;
    private String photoUrl;
    private String backgroundDrawable;
    private boolean firstTimeIn;
    private String firstName;
    private String lastName;
    private String auth;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String username, String firstName, String lastName, String auth) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.auth = auth;
    }


    public User(String displayName, String uid, String email) {
        this.displayName = displayName;
        this.uid = uid;
        this.email = email;
    }


//    public User(String uid, String email, String username, String displayName, String photoUrl) {
//        this.uid = uid;
//        this.email = email;
//        this.username = username;
//        this.displayName = displayName;
//        this.photoUrl = photoUrl;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBackgroundDrawable() {
        return backgroundDrawable;
    }

    public void setBackgroundDrawable(String backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
    }

    public boolean isFirstTimeIn() {
        return firstTimeIn;
    }

    public void setFirstTimeIn(boolean firstTimeIn) {
        this.firstTimeIn = firstTimeIn;
    }

    public String getFirstName() { return this.firstName; }

    public String getLastName() { return this.lastName; }

    public String getAuth() { return this.auth; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setAuth(String auth) { this.auth = auth; }
}
