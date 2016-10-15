package com.koshka.origami.activity.settings.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.adapter.fragment.UISettingsFragmentPagerAdapter;
import com.koshka.origami.utils.SharedPrefs;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class UISettingsActivity extends GenericOrigamiActivity {

    @BindView(R.id.ui_settings_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout smartTab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_settings_activity);
        ButterKnife.bind(this);

        uiSetup();
    }

    @Override
    protected void serviceSetup() {

    }

    @Override
    protected void uiSetup() {

        fragmentSetup(mPager, FragmentAdapters.UI_SETTINGS, 0, smartTab);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
