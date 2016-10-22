package com.koshka.origami.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by qm0937 on 10/15/16.
 */

public abstract class GenericFragmentPagerAdapter extends FragmentPagerAdapter {


    public GenericFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return getmFragments()[position];
    }

    @Override
    public int getCount() {
        return getmFragments().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getFragmentNames()[position];
    }

    protected abstract String[] getFragmentNames();
    protected abstract Fragment[] getmFragments();
}
