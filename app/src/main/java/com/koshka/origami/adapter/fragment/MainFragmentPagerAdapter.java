package com.koshka.origami.adapter.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.main.FriendsFragment;
import com.koshka.origami.fragment.main.OrigamiFragment;
import com.koshka.origami.fragment.main.OrigamiMapFragment;

/**
 * Created by imuntean on 7/31/16.
 */
public class MainFragmentPagerAdapter extends GenericFragmentPagerAdapter {

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new OrigamiMapFragment(), new OrigamiFragment(), new FriendsFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "Map", "Origami", "Friends",
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
