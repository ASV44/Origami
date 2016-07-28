package com.koshka.origami.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.DatabaseRefUtil;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.fragment.main.FriendsFragment;
import com.koshka.origami.fragment.main.OrigamiFragment;
import com.koshka.origami.placepicker.GooglePlacePickerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private final static String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private int backButtonCount;

    @BindView(R.id.mainViewPager)
    ViewPager mViewPager;

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.multiple_actions2)
    FloatingActionsMenu floatingMenu;

    @BindView(R.id.create_origami_button)
    FloatingActionButton createOrigamiButton;

    @BindView(R.id.add_friend_button)
    FloatingActionButton addFriendButton;

    @BindView(R.id.invite_friend_button)
    FloatingActionButton inviteFriendButton;

    @BindView(R.id.tabs)
    TabLayout tabLayout;


    private DatabaseReference connectedRef;
    private ValueEventListener isConnectedListener;

    private boolean init = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewPager.setAdapter(getmPagerAdapter());
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);

        decideMenuButtonElements(mViewPager.getCurrentItem());

        tabLayout.setupWithViewPager(mViewPager);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }

        connectedRef = DatabaseRefUtil.getmConnectedRef();
        isConnectedListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.material_red_a200)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.material_red_a2001));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Listener was cancelled");
            }
        };

        connectedRef.addValueEventListener(isConnectedListener);


    }

    // Create the adapter that will return a fragment for each section
    private FragmentPagerAdapter getmPagerAdapter() {

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new OrigamiFragment(),
                    new FriendsFragment(),


            };
            private final String[] mFragmentNames = new String[]{
                    "Origami",
                    "Friends",
            };

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        return mPagerAdapter;
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
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
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                return true;
            case R.id.map_view:
                startActivity(new Intent(MainActivity.this, OrigamiMapActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Resources res = getResources();
            Snackbar.make(mRootView, res.getString(R.string.press_back), Snackbar.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    @MainThread
    private void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      decideMenuButtonElements(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void decideMenuButtonElements(int position){
        if (position == 0){
            addFriendButton.setVisibility(View.GONE);
            inviteFriendButton.setVisibility(View.GONE);
            createOrigamiButton.setVisibility(View.VISIBLE);
            floatingMenu.collapse();
        } else {
            createOrigamiButton.setVisibility(View.GONE);
            addFriendButton.setVisibility(View.VISIBLE);
            inviteFriendButton.setVisibility(View.VISIBLE);
            floatingMenu.collapse();
        }
    }

    @OnClick(R.id.create_origami_button)
    public void createOrigami(View view){
        startActivity(new Intent(this, GooglePlacePickerActivity.class));
    }
}
