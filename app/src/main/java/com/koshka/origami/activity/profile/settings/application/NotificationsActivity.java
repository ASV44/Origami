package com.koshka.origami.activity.profile.settings.application;

import android.os.Bundle;

import com.koshka.origami.R;
import com.koshka.origami.activity.AppCompatBase;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class NotificationsActivity extends AppCompatBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notifications_settings_layout);
        ButterKnife.bind(this);

    }
}
