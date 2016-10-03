package com.koshka.origami.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.fragment.before_main.FirstLoginFragmentPagerAdapter;
import com.koshka.origami.fragment.main.MainFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.id.message;

/**
 * Created by qm0937 on 10/3/16.
 */

public class FirstTimeLaunchPreferencesActivity extends AppCompatActivity {

    @BindView(R.id.first_time_login_pager)
    ViewPager viewPager;

    private FirstLoginFragmentPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.first_login_activity);
        ButterKnife.bind(this);


        pagerAdapter = new FirstLoginFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

        public void goToNextFragment(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() +1, true);

    }


}
