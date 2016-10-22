package com.koshka.origami.fragments.profile;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.DatabaseRefUtil;
import com.google.firebase.database.DatabaseReference;
import com.koshka.origami.R;
import com.koshka.origami.fragments.GenericOrigamiFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

/**
 * Created by imuntean on 8/6/16.
 */
public class UserProfileFragmentMain extends GenericOrigamiFragment {

    private static final String TAG = "ProfileFragment";

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.profile_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.user_display_name)
    AutofitTextView mUserDisplayName;

    //----------------------------------------------------------------------------------------------

    private DatabaseReference mMeRef;

    private boolean mapIsMaximized = false;


    //----------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);


        mMeRef = DatabaseRefUtil.getUserRef(mAuth.getCurrentUser().getUid());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateProfile();
    }

    //----------------------------------------------------------------------------------------------


    @MainThread
    private void populateProfile() {
        String nickname = mAuth.getCurrentUser().getDisplayName();
       /* mUserEmail.setText(email);*/
        mUserDisplayName.setText(nickname);
/*
        if (mAuth.getCurrentUser().getPhotoUrl() != null) {
            Glide.with(getActivity().getApplicationContext())
                    .load(mAuth.getCurrentUser().getPhotoUrl())
                    .fitCenter()
                    .into(mUserProfilePicture);
        }*/

    }



}
