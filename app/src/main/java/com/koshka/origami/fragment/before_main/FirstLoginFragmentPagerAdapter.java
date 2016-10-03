package com.koshka.origami.fragment.before_main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.profile.UserProfileFragmentMain;
import com.koshka.origami.fragment.profile.UserProfileFragmentSettings;

/**
 * Created by qm0937 on 10/3/16.
 */

public class FirstLoginFragmentPagerAdapter extends FragmentPagerAdapter {

    public FirstLoginFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new ColorPickerFragment(),
            new NamePickerFragment(),
            new TutorialFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "Color Picker",
            "Name Picker",
            "TutorialFragment"
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