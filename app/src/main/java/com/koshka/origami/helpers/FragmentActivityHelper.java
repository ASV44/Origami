package com.koshka.origami.helpers;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.koshka.origami.R;
import com.koshka.origami.factory.FragmentPagerAdapterFactory;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by qm0937 on 10/19/16.
 */

public class FragmentActivityHelper {

    private FragmentActivity fragmentActivity;

    public FragmentActivityHelper(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }


    //Util method for building a pager adapter using the factory
    private FragmentPagerAdapter buildPagerAdapter(String pagerAdapter) {
        return FragmentPagerAdapterFactory.build(pagerAdapter, fragmentActivity.getSupportFragmentManager());
    }

    //Just a faster way to set view pager and smart tab layout
    public void fragmentSetup(ViewPager viewPager, String adapter, int currentFragment, SmartTabLayout smartTabLayout) {

        viewPager.setAdapter(buildPagerAdapter(adapter));
        viewPager.setCurrentItem(currentFragment);


        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.recycler_view));
        viewPager.setPageTransformer(false, pt);

        if (smartTabLayout != null) {
            smartTabLayout.setViewPager(viewPager);
        }


    }
}
