package com.koshka.origami.activity.settings.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 8/11/16.
 */
public class UISettingsActivity extends AppCompatActivity {

    @BindView(R.id.ui_settings_view_pager)
    ViewPager viewPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout viewpagertab;

    public static final String SHARED_PREFS = "SharedPrefs";
    private UISettingsFragmentPagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int theme = prefs.getInt("theme", -1);

        setTheme(theme);


        setContentView(R.layout.ui_settings_activity);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pagerAdapter = new UISettingsFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewpagertab.setViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // back button
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                return false;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
