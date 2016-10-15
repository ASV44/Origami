package com.koshka.origami.adapter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.main.ChatFragment;
import com.koshka.origami.fragment.main.FriendsFragment;
import com.koshka.origami.fragment.main.OrigamiFragment;
import com.koshka.origami.fragment.profile.UserProfileFragmentMain;
import com.koshka.origami.fragment.profile.UserProfileFragmentSettings;

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