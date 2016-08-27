package com.koshka.origami.activity.origami;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/27/16.
 */
public class OpenedOrigamiActivity extends AppCompatActivity {

    @BindView(R.id.some_text_view)
    TextView usernameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_origami_opened);
        ButterKnife.bind(this);

        final String username = getIntent().getStringExtra("username");

        if (username != null){
            usernameTextView.setText(username);
        }
    }
}
