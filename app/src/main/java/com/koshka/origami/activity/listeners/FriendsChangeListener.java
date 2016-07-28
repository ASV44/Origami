package com.koshka.origami.activity.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by imuntean on 7/28/16.
 */
public interface FriendsChangeListener extends ValueEventListener {

    @Override
    void onDataChange(DataSnapshot dataSnapshot);

    @Override
    void onCancelled(DatabaseError databaseError);
}
