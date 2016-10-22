package com.koshka.origami.activites.friends;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activites.login.LoginActivity;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/26/16.
 */
public class InviteFriendActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_invite_friend);
        ButterKnife.bind(this);
    }
}
