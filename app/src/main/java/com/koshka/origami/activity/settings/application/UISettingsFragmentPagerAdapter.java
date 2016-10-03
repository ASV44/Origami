package com.koshka.origami.activity.settings.application;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.fragment.before_main.ColorPickerFragment;
import com.koshka.origami.fragment.before_main.NamePickerFragment;
import com.koshka.origami.fragment.before_main.TutorialFragment;
import com.koshka.origami.fragment.settings.ui.BackgroundColorPickFragment;

/**
 * Created by qm0937 on 10/3/16.
 */

public class UISettingsFragmentPagerAdapter  extends FragmentPagerAdapter {

    public UISettingsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final Fragment[] mFragments = new Fragment[]{
            new BackgroundColorPickFragment(),
            new OtherUISettingsFragment(),


    };
    private final String[] mFragmentNames = new String[]{
            "Background",
            "Style",
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
