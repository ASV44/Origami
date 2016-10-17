package com.koshka.origami.activity.profile.settings.about;

import android.os.Bundle;

import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;

import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class FAQActivity extends GenericOrigamiActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.faq_layout);
        ButterKnife.bind(this);

    }
}
