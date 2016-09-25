package com.koshka.origami.fragment.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by imuntean on 7/31/16.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new OrigamiMapFragment(),
            new OrigamiFragment(),
            new FriendsFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "Map",
            "Origami",
            "Friends",
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
