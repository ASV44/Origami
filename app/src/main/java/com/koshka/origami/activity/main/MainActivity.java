package com.koshka.origami.activity.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koshka.origami.R;
import com.koshka.origami.activity.login.LoginActivity;
import com.koshka.origami.fragment.main.MainFragmentPagerAdapter;
import com.koshka.origami.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final static String TAG = "MainActivity";

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.main_pager)
    ViewPager mPager;

    private int backButtonCount;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean locationPermissionGranted = false;
    private boolean mPermissionDenied = false;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.action_bar_title, null);

            TextView titleTV = (TextView) v.findViewById(R.id.title_main);
            Typeface font = Typeface.createFromAsset(getAssets(),
                    "fonts/actonia.ttf");
            titleTV.setTypeface(font);
            actionBar.setCustomView(v);

        }
/*
        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.friends_recycler_view));
        pt.setBorder(3);
        pt.setSpeed(0.7f);
        mPager.setPageTransformer(false, pt);*/
        mPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(1);
        enableMyLocation();

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
                intent.setClass(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.map_view:
                startActivity(new Intent(MainActivity.this, OrigamiMapActivity.class));
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else  {
            // Access to the location has been granted to the app.
            locationPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
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
            showShortSnackbar(R.string.press_back);
            backButtonCount++;
        }
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



/*    @OnClick(R.id.create_origami_button)
    public void createOrigami(View view) {
        startActivity(new Intent(this, GooglePlacePickerActivity.class));
    }

    @OnClick(R.id.add_friend_button)
    public void addFriend(View view) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mMe = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());
        final DatabaseReference mMyFriends = mMe.child("friendList");
        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertUserInfoDialog = new AlertDialog.Builder(MainActivity.this);

        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Add friend");
        alertDialog.setIcon(R.drawable.origami);
        alertDialog.setMessage("Enter friend's email:");
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String email = input.getText().toString();
                if (email != null && !email.isEmpty()) {
                    final Query query = DatabaseRefUtil.getFindUserByEmailQuery(input.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final User user = dataSnapshot.getValue(User.class);
                                        if (!user.getEmail().equals(mAuth.getCurrentUser().getEmail())){

                                        alertUserInfoDialog.setTitle("Is that him?");
                                        alertUserInfoDialog.setMessage(user.getDisplayName() + ", " + user.getEmail()+ "," + user.getUid());

                                        alertUserInfoDialog.setPositiveButton("Yes, that's him", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Friend friend = new Friend();
                                                friend.setDisplayName(user.getDisplayName());
                                                friend.setEmail(user.getEmail());
                                                friend.setUid(user.getUid());
                                                mMyFriends.push().setValue(friend);
                                            }
                                        });
                                        alertUserInfoDialog.create();
                                        alertUserInfoDialog.show();
                                    }else {

                                            alertUserInfoDialog.setMessage("That seems to be you. :)");
                                            alertUserInfoDialog.create();
                                            alertUserInfoDialog.show();
                                    }

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                alertUserInfoDialog.setTitle("No such user in Origami");
                                alertUserInfoDialog.setMessage("Do you want to invite " + input.getText().toString() + "?");
                                alertUserInfoDialog.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                alertUserInfoDialog.create();
                                alertUserInfoDialog.show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        alertDialog.create();
        alertDialog.show();
    }*/

/*
            @OnClick(R.id.invite_friend_button)
            public void inviteFriend(View view) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final DatabaseReference mMe = DatabaseRefUtil.getUserRefByUid(mAuth.getCurrentUser().getUid());
                final DatabaseReference mMyFriends = mMe.child("friendList");
                // Creating alert Dialog with one Button
                final AlertDialog.Builder alertUserInfoDialog = new AlertDialog.Builder(MainActivity.this);

                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                alertDialog.setTitle("Invite friend to Origami");
                alertDialog.setMessage("Enter friend's email:");
                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input != null) {
                            Query query = DatabaseRefUtil.getFindUserByEmailQuery(input.getText().toString());
                            query.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final User user = dataSnapshot.getValue(User.class);

                                    alertUserInfoDialog.setTitle("Is that him?");
                                    alertUserInfoDialog.setMessage(user.getDisplayName());

                                    alertUserInfoDialog.setPositiveButton("Yes, that's him", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mMyFriends.push().setValue(user.getUid());
                                        }
                                    });

                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            alertUserInfoDialog.create();
                            alertUserInfoDialog.show();

                        } else {
                            showShortSnackbar(R.string.no_place);
                        }

                    }
                });
                alertDialog.create();
                alertDialog.show();

            }*/


}

