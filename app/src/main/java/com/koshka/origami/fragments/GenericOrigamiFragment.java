package com.koshka.origami.fragments;


import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.helpers.fragment.FragmentHelper;

/**
 * Created by qm0937 on 10/16/16.
 */

public class GenericOrigamiFragment extends Fragment {

    protected static final String TAG = "GenericFragment";


    //----------------------------------------------------------------------------------------------

    protected FirebaseAuth mAuth;
    protected FirebaseUser currentUser;

    //----------------------------------------------------------------------------------------------

    protected FragmentHelper fragmentHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Generic_Fragment","initiate");
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("Generic_Fragment","Current_User -> " + currentUser);

        fragmentHelper = new FragmentHelper(getActivity());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //----------------------------------------------------------------------------------------------


    @MainThread
    public void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(getView(), errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }
}
