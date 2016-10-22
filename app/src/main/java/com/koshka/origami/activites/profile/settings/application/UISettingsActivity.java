package com.koshka.origami.activites.profile.settings.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.koshka.origami.R;
import com.koshka.origami.adapters.fragment.FragmentAdapters;
import com.koshka.origami.helpers.activity.FragmentActivityHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class UISettingsActivity extends AppCompatActivity {

    @BindView(R.id.ui_settings_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout smartTab;

    //----------------------------------------------------------------------------------------------

    private FragmentActivityHelper fragmentActivityHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_ui_activity);
        ButterKnife.bind(this);

        fragmentActivityHelper = new FragmentActivityHelper(this);

        fragmentActivityHelper.fragmentSetup(mPager, FragmentAdapters.UI_SETTINGS, 0, smartTab);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
