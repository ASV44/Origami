package com.firebase.ui.database.service;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Created by qm0937 on 10/7/16.
 */

public interface UserService {

    boolean reauthenticate(String email, String password);
    void deleteFriendsNode();
    void deleteUserNode();
    void deletePreferenceNode();
    boolean deleteAllUserNodes();
    boolean deleteUser(Activity activity, Class activityToGo);
    void changeEmail(String email);
    void changeUsername(String username);
    void firstTimeInTrue();
    void firstTimeInToFalse();
}
