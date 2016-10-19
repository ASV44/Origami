package com.koshka.origami.helpers;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.koshka.origami.adapter.fragment.FragmentAdapters;
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
    }


    public boolean isNetworkOn() {
        return NetworkUtil.isNetworkConnected(activity);
    }



    }
