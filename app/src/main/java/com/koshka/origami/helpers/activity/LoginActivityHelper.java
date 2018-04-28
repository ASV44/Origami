package com.koshka.origami.helpers.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.koshka.origami.adapters.fragment.FragmentAdapters;
import com.koshka.origami.fragments.login.FirstPageFragment3;
import com.koshka.origami.helpers.OrigamiActionBarHelper;
import com.koshka.origami.helpers.fragment.FacebookLogInHelper;
import com.koshka.origami.utils.net.NetworkUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by qm0937 on 10/19/16.
 */

public class LoginActivityHelper {
    private AppCompatActivity activity;
    private FragmentActivityHelper activityHelper;



    private static final String adapter = FragmentAdapters.LOGIN;

    public LoginActivityHelper(AppCompatActivity activity) {
        this.activity = activity;
        activityHelper = new FragmentActivityHelper(activity);
    }

    public void toolbarSetup(Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        OrigamiActionBarHelper.setMainActivityAttributes(activity, activity.getSupportActionBar());
    }

    public void fragmentSetup(ViewPager mPager, SmartTabLayout mSmartTab) {
        activityHelper.fragmentSetup(mPager, adapter, 1, mSmartTab);
        getFragment3().setActivity(activity);
    }


    public boolean isNetworkOn() {
        return NetworkUtil.isNetworkConnected(activity);
    }

    public FirstPageFragment3 getFragment3() {
        Fragment[] mFragments = activityHelper.getmFragments();
        FirstPageFragment3 fragment3 = (FirstPageFragment3) mFragments[2];
        return  fragment3;
    }


}
