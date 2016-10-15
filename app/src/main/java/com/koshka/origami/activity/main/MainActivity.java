package com.koshka.origami.activity.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.DatabaseRefUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.activity.login.FirstTimeLoginActivity;
import com.firebase.ui.database.service.PreferenceServiceImpl;
import com.firebase.ui.database.service.UserServiceImpl;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.adapter.fragment.MainFragmentPagerAdapter;
import com.koshka.origami.helpers.OrigamiActionBarHelper;
import com.koshka.origami.helpers.TypeFaceHelper;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;
import com.koshka.origami.utils.SharedPrefs;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends GenericOrigamiActivity {

    private final static String TAG = "MainActivity";

    private static final int FIRST_LOGIN_PREFS_REQUEST = 11;

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout mSmartTab;

    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;

    @BindView(R.id.text_origami_logo)
    TextView origamiLogoTextView;

    private OrigamiThemeHelper helper;
    private UserServiceImpl userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        serviceSetup();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        uiSetup();

    }

    @Override
    protected void serviceSetup() {

        userService = new UserServiceImpl();
        helper = new OrigamiThemeHelper(this);
    }

    @Override
    protected void uiSetup() {
        firstTimeUse();

        TypeFaceHelper.setTypeFace(this, origamiLogoTextView, TypeFaceHelper.origamiLogoTypeFace);
        loadingPageStart();

        setSupportActionBar(mToolbar);

        OrigamiActionBarHelper.setMainActivityAttributes(this, getSupportActionBar());

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.recycler_view));
        mPager.setPageTransformer(false, pt);

        fragmentSetup(mPager, FragmentAdapters.MAIN, 1, mSmartTab);
    }


    //StartLoading Page on another thread to prepare everything
    private void loadingPageStart() {

        retrieveUserPreferencesFromDb(loadingLayout);
        retrieveUserPreferencesFromDb(mRootView);

        loadingLayout.postDelayed(new Runnable() {
            public void run() {

                loadingLayout.animate().alpha(0).setDuration(1000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        loadingLayout.setVisibility(View.GONE);
                    }
                });

            }
        }, 4000);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.map_view:
                startActivity(new Intent(this, OrigamiMapActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FIRST_LOGIN_PREFS_REQUEST: {
                userService.firstTimeInToFalse();
                SharedPrefs.changeBackground(this, mRootView);
                break;
            }

        }
    }

    private void firstTimeUse() {

        DatabaseReference mUsernameRef = DatabaseRefUtil.getUserRef(currentUser.getUid());

        mUsernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean firstTimeIn = (Boolean) dataSnapshot.child("firstTimeIn").getValue();
                if (firstTimeIn != null) {
                    if (firstTimeIn) {
                        firstTimeLoginPageStart();
                    } else {
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firstTimeLoginPageStart() {
        Intent intent = new Intent(this, FirstTimeLoginActivity.class);
        startActivityForResult(intent, FIRST_LOGIN_PREFS_REQUEST);

    }

    private void retrieveUserPreferencesFromDb(final View view) {

        DatabaseReference mUsernameRef = FirebaseDatabase.getInstance().getReference().child("prefs").child(currentUser.getUid()).child("backgroundColor");

        mUsernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long backgroundColor = (Long) dataSnapshot.getValue();
                if (backgroundColor != null) {
                    int integer = new BigDecimal(backgroundColor).intValueExact();
                    if (integer != -1) {
                       helper.firebaseSetAndSaveTheme(integer);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }

}


