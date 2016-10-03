package com.firebase.ui.database;

/**
 * Created by imuntean on 8/29/16.
 */
public enum RefConstants {

    users("users"),
    friends("friends"),
    preferences("prefs"),
    public_origami("public_origami"),
    user_current_location("user_current_location"),
    connected(".info/connected");

    private String name;

    RefConstants(String ref){
            name = ref;

    }

}
