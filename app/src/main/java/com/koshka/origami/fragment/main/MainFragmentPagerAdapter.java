package com.koshka.origami.fragment.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.login.LoginFragment1;
import com.koshka.origami.fragment.login.LoginFragment2;
import com.koshka.origami.fragment.login.LoginFragment3;

/**
 * Created by imuntean on 7/31/16.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new ChatFragment(),
            new OrigamiFragment(),
            new FriendsFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "1",
            "2",
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
