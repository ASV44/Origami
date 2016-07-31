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
    private String country;
    private String languagePreference;
    private Map<String,Friend> friendList;
    private Coordinate currentLocation;

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }

    public User(String displayName, String uid, String email) {
        this.displayName = displayName;
        this.uid = uid;
        this.email = email;
    }

    public User(String uid, String email, String username, String country, String displayName) {
        this.uid = uid;
        this.email = email;
        this.nickname = username;
        this.country = country;
        this.displayName = displayName;
    }

    public Map<String, Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(Map<String, Friend> friendList) {
        this.friendList = friendList;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Coordinate getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinate currentLocation) {
        this.currentLocation = currentLocation;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

}
