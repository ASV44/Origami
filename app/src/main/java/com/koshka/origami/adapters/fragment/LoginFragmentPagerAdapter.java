package com.koshka.origami.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.facebook.CallbackManager;
import com.koshka.origami.fragments.login.FirstPageFragment1;
import com.koshka.origami.fragments.login.FirstPageFragment2;
import com.koshka.origami.fragments.login.FirstPageFragment3;

/**
 * Created by imuntean on 7/31/16.
 */
public class LoginFragmentPagerAdapter extends GenericFragmentPagerAdapter {

    public LoginFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new FirstPageFragment2(), new FirstPageFragment1(), new FirstPageFragment3(),


    };
    private final String[] mFragmentNames = new String[]{
            "1", "2", "3",
    };

    @Override
    protected String[] getFragmentNames() {
        return mFragmentNames;
    }

    @Override
    public Fragment[] getmFragments() {
        return mFragments;
    }

}
