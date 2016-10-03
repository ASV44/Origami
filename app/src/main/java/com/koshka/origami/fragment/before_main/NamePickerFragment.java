package com.koshka.origami.fragment.before_main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koshka.origami.R;
import com.koshka.origami.activity.tutorial.FirstTimeLaunchPreferencesActivity;
import com.koshka.origami.model.UserPreferences;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qm0937 on 10/3/16.
 */

public class NamePickerFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name_picker_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.name_pick_next_button)
    public void next(){
        FirstTimeLaunchPreferencesActivity activity = (FirstTimeLaunchPreferencesActivity)getActivity();
        activity.goToNextFragment();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("prefs").child(mAuth.getCurrentUser().getUid()).child("shownName");
        ref.setValue("test").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }
}
