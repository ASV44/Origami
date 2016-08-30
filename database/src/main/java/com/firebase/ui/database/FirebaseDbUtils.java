package com.firebase.ui.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by imuntean on 8/29/16.
 */
public class FirebaseDbUtils {

    public static boolean removeEventListener(DatabaseReference ref, ValueEventListener valueEventListener) {

        if (valueEventListener != null) {
            ref.removeEventListener(valueEventListener);
            return true;
        } else {
            return false;
        }

    }

    public static boolean removeEventListenerList(DatabaseReference ref, List<ValueEventListener> valueEventListenerList) {

        if (valueEventListenerList != null && !valueEventListenerList.isEmpty()) {
            for (ValueEventListener listener : valueEventListenerList) {
                ref.removeEventListener(listener);
            }
            return true;
        } else {
            return false;
        }

    }

    public static boolean removeChildEventListenr(DatabaseReference ref, ChildEventListener childEventListener) {

        if(childEventListener != null){

            ref.removeEventListener(childEventListener);
            return true;
        } else {
            return false;
        }

    }
}
