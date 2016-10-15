package com.koshka.origami.factory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.koshka.origami.adapter.fragment.EmptyFragmentPagerAdapter;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.adapter.fragment.LoginFragmentPagerAdapter;
import com.koshka.origami.adapter.fragment.MainFragmentPagerAdapter;
import com.koshka.origami.adapter.fragment.UISettingsFragmentPagerAdapter;
import com.koshka.origami.adapter.fragment.UserProfileFragmentPagerAdapter;

/**
 * Created by qm0937 on 10/15/16.
 */

public class FragmentPagerAdapterFactory {

    public static FragmentPagerAdapter build(String adapter, FragmentManager manager){

        switch (adapter){
            case FragmentAdapters.LOGIN:{
                return new LoginFragmentPagerAdapter(manager);
            }
            case FragmentAdapters.MAIN:{
                return new MainFragmentPagerAdapter(manager);
            }
            case FragmentAdapters.PROFILE:{
                return new UserProfileFragmentPagerAdapter(manager);
            }
            case FragmentAdapters.UI_SETTINGS:{
                return new UISettingsFragmentPagerAdapter(manager);
            }
            default:{
                return new EmptyFragmentPagerAdapter(manager);
            }
        }
    }

}
