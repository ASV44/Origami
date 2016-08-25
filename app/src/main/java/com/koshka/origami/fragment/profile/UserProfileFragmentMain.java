package com.koshka.origami.fragment.profile;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;

/**
 * Created by imuntean on 8/6/16.
 */
public class UserProfileFragmentMain extends Fragment {

/*    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;

    @BindView(R.id.user_email)
    TextView mUserEmail;*/

    @BindView(R.id.user_display_name)
    AutofitTextView mUserDisplayName;


    private DatabaseReference mMeRef;
    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_main, container, false);
        ButterKnife.bind(this, view);


        mAuth = FirebaseAuth.getInstance();
        mMeRef = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Typeface font = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/Hang.ttf");

        mUserDisplayName.setTypeface(font);
        populateProfile();
    }



    @MainThread
    private void populateProfile() {
        String email = mAuth.getCurrentUser().getEmail();
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

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(getView(), errorMessageRes, Snackbar.LENGTH_LONG)
                .show();
    }


}
