package com.koshka.origami.activity.settings.about;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.utils.SharedPrefs;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class ToSActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPrefs.changeTheme(this);
        setContentView(R.layout.tos_layout);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
