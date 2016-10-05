package com.koshka.origami.activity.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.DatabaseRefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.activity.tutorial.FirstTimeLaunchPreferencesActivity;
import com.koshka.origami.fragment.main.MainFragmentPagerAdapter;
import com.koshka.origami.helpers.OrigamiActionBarHelper;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.koshka.origami.utils.ui.ViewThemeUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    public static final String SHARED_PREFS = "SharedPrefs";

    private static final int FIRST_LOGIN_PREFS_REQUEST = 11;

    @BindView(android.R.id.content)
    View mRootView;

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

    private FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        beforeStartingCheck();

        setTypeFace();
        loadingPageStart();

        setUI();


    }

    private void setUI(){

        setSupportActionBar(mToolbar);


        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.recycler_view));
        pt.setBorder(0);
        pt.setSpeed(0.7f);
        mPager.setPageTransformer(false, pt);

        OrigamiActionBarHelper.setMainAttributes(this, getSupportActionBar());


        mPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(1);

        mSmartTab.setViewPager(mPager);
    }

    private void beforeStartingCheck() {
        firstTimeUse();


    }

    //StartLoading Page on another thread to prepare everything
    private void loadingPageStart() {

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int backgroundInt = prefs.getInt("gradient", -1);

        if (backgroundInt  != -1){
            loadingLayout.setBackground(getResources().getDrawable(backgroundInt));
            mRootView.setBackground(getResources().getDrawable(backgroundInt));
        }

        setUpBackgroundFromFirebaseForView(mRootView);
        setUpBackgroundFromFirebaseForView(loadingLayout);

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

    private void setTypeFace() {
        final Typeface font = Typeface.createFromAsset(getAssets(), "fonts/origamibats.ttf");

        origamiLogoTextView.setTypeface(font);
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
                Intent intent = new Intent();
                intent.setClass(this, UserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.map_view:
                startActivity(new Intent(this, OrigamiMapActivity.class));
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FIRST_LOGIN_PREFS_REQUEST: {
                if(resultCode == RESULT_OK){

                    afterFirstTimeLogin();
                SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                int backgroundGradient = prefs.getInt("gradient", -1);
                getWindow().setBackgroundDrawable(getResources().getDrawable(backgroundGradient));
                }
            }
                break;
            }

        }

    private void afterFirstTimeLogin() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = DatabaseRefUtil.getUserRef(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstTimeIn");
        ref.setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }



    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, MainActivity.class);
        return in;
    }

    @MainThread
    private void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }


    private void firstTimeUse() {

        DatabaseReference mUsernameRef = DatabaseRefUtil.getUserRef(currentUser.getUid());

        mUsernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean firstTimeIn = (Boolean) dataSnapshot.child("firstTimeIn").getValue();
                if (firstTimeIn != null) {
                    if (firstTimeIn) {
                        doSomething();
                    } else {
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //startActivityForResult();

    }

    private void setUpBackgroundFromFirebaseForView(final View view) {

        DatabaseReference mUsernameRef = FirebaseDatabase.getInstance().getReference().child("prefs").child(currentUser.getUid()).child("backgroundColor");


        mUsernameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long backgroundColor = (Long) dataSnapshot.getValue();
                if (backgroundColor != null) {
                    int integer = new BigDecimal(backgroundColor).intValueExact();
                    if (integer != -1){
                        setBackgroundForView(view, integer);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void doSomething() {
        Intent intent = new Intent(this, FirstTimeLaunchPreferencesActivity.class);
        startActivityForResult(intent, FIRST_LOGIN_PREFS_REQUEST);

    }

    private void setBackgroundForView(View view, int firebaseBackgroundInt) {
        ViewThemeUtil util = new ViewThemeUtil(this, view);
        util.setBackgroundToViewAndSavePreference(firebaseBackgroundInt);

    }

}


