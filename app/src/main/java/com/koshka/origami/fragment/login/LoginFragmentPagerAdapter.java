package com.koshka.origami.fragment.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by imuntean on 7/31/16.
 */
public class LoginFragmentPagerAdapter extends FragmentPagerAdapter {

    public LoginFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new LoginFragment1(),
            new LoginFragment2(),
            new LoginFragment3(),


    };
    private final String[] mFragmentNames = new String[]{
            "1",
            "2",
            "3",
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
