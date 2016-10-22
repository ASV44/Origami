package com.koshka.origami.activites.profile.settings.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.koshka.origami.R;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class LicencesActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_licences_activity);
        ButterKnife.bind(this);


    }
}
