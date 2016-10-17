package com.koshka.origami.activity.login;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.fragment.firstlogin.FirstLoginFragmentPagerAdapter;
import com.koshka.origami.utils.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qm0937 on 10/3/16.
 */

public class FirstTimeLoginActivity extends GenericOrigamiActivity {

    @BindView(R.id.first_time_login_pager)
    ViewPager viewPager;

    private FirstLoginFragmentPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkFirebase(this);

        setContentView(R.layout.first_login_activity);
        ButterKnife.bind(this);

    }

        public void goToNextFragment(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() +1, true);

    }


}
