package com.koshka.origami.activites.login;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qm0937 on 10/3/16.
 */

public class FirstTimeLoginActivity extends OrigamiActivity {

    @BindView(R.id.first_time_login_pager)
    ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity);
        ButterKnife.bind(this);

    }

    public void goToNextFragment() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);

    }


}
