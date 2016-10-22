package com.koshka.origami.activites.profile.settings.about;

import android.os.Bundle;

import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class ToSActivity extends OrigamiActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tos_activity);
        ButterKnife.bind(this);

    }
}
