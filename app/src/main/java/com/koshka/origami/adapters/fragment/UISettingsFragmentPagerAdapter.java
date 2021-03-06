package com.koshka.origami.adapters.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.koshka.origami.fragments.settings.ui.OtherUISettingsFragment;
import com.koshka.origami.fragments.settings.ui.BackgroundColorPickFragment;

/**
 * Created by qm0937 on 10/3/16.
 */

public class UISettingsFragmentPagerAdapter  extends GenericFragmentPagerAdapter {

    public UISettingsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new BackgroundColorPickFragment(), new OtherUISettingsFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "Background", "Style",
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
