package com.koshka.origami.activity.login;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.koshka.origami.R;
import com.koshka.origami.activity.AppCompatBase;
import com.koshka.origami.fragment.firstlogin.FirstLoginFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qm0937 on 10/3/16.
 */

public class FirstTimeLoginActivity extends AppCompatBase {

    @BindView(R.id.first_time_login_pager)
    ViewPager viewPager;

    private FirstLoginFragmentPagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_login_activity);
        ButterKnife.bind(this);

    }

    public void goToNextFragment() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

    }


}
