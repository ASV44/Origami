package com.koshka.origami.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.koshka.origami.fragments.main.friends.FriendsFragment;
import com.koshka.origami.fragments.main.origami.OrigamiFragment;
import com.koshka.origami.fragments.main.map.OrigamiMapFragment;

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
