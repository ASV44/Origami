package com.koshka.origami.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.koshka.origami.fragments.profile.UserProfileFragmentMain;
import com.koshka.origami.fragments.profile.UserProfileFragmentSettings;

/**
 * Created by imuntean on 8/6/16.
 */
public class UserProfileFragmentPagerAdapter extends GenericFragmentPagerAdapter {

    public UserProfileFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new UserProfileFragmentMain(), new UserProfileFragmentSettings(),


    };
    private final String[] mFragmentNames = new String[]{
            "Profile", "Settings",
    };


    @Override
    protected String[] getFragmentNames() {
        return mFragmentNames;
    }

    @Override
    protected Fragment[] getmFragments() {
        return mFragments;
    }


}