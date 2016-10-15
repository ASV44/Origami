package com.koshka.origami.adapter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by qm0937 on 10/15/16.
 */

public class EmptyFragmentPagerAdapter extends GenericFragmentPagerAdapter {

    public EmptyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    protected String[] getFragmentNames() {
        return new String[0];
    }

    @Override
    protected Fragment[] getmFragments() {
        return new Fragment[0];
    }
}
