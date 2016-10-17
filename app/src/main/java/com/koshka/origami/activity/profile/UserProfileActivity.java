package com.koshka.origami.activity.profile;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/19/16.
 */
public class UserProfileActivity extends GenericOrigamiActivity {

    private static final String TAG = "UserProfileActivity";

    private static final String FRAGMENT_ADAPTERS = FragmentAdapters.PROFILE;

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout viewpagertab;

    @BindView(R.id.profile_pager)
    ViewPager mPager;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkFirebase(this);

        setContentView(R.layout.user_profile_layout);
        ButterKnife.bind(this);

        fragmentSetup(mPager, FRAGMENT_ADAPTERS, 0, viewpagertab);

    }

}
