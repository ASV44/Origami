package com.koshka.origami.fragment.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.main.ChatFragment;
import com.koshka.origami.fragment.main.FriendsFragment;
import com.koshka.origami.fragment.main.OrigamiFragment;

/**
 * Created by imuntean on 8/6/16.
 */
public class UserProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    public UserProfileFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new UserProfileFragmentMain(),
            new UserProfileFragmentSettings(),


    };
    private final String[] mFragmentNames = new String[]{
            "Profile",
            "Settings",
    };

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNames[position];
    }
}