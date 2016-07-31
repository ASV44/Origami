package com.koshka.origami.model;

/**
 * Created by imuntean on 7/20/16.
 */
public class Friend {

    private String uid;
    private String email;
    private String displayName;
    private String nickname;

    public Friend() {
    }

    public Friend(String uid, String email, String displayName, String nickname) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.nickname = nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
