package com.koshka.origami.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.koshka.origami.R;
import com.koshka.origami.activity.GenericOrigamiActivity;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.adapter.fragment.FragmentAdapters;
import com.koshka.origami.adapter.fragment.LoginFragmentPagerAdapter;
import com.koshka.origami.utils.net.NetworkUtil;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by imuntean on 7/19/16.
 */
public class LoginActivity extends GenericLoginActivity {

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 100;

    //----------------------------------------------------------------------------------------------

    @BindView(R.id.login_pager)
    ViewPager mPager;

    @BindView(R.id.sign_in)
    Button mSignIn;

    @BindView(R.id.title_text)
    TextView titleTextView;

    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;

    //----------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        fragmentSetup(mPager, FragmentAdapters.LOGIN, 0, null);

        circleIndicator.setViewPager(mPager);

        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/actonia.ttf");

        if (font != null) {
            titleTextView.setTypeface(font);
        }


    }

    //----------------------------------------------------------------------------------------------

    @OnClick(R.id.sign_in)
    public void signIn(View view) {

        if (isNetworkOn()) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setTheme(getSelectedTheme())
                            .setLogo(getSelectedLogo())
                            .setProviders(getSelectedProviders())
                            .setTosUrl(getSelectedTosUrl())
                            .build(),
                    RC_SIGN_IN);

        } else {
            showSnackbar(R.string.no_internet_connection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }
        showSnackbar(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }
        //TODO:UNKOWN SIGN IN RESPONSE, HANDLE THIS BY SENDING USER TO SUPPORT OR HELP PAGE.
        showSnackbar(R.string.unknown_sign_in_response);
    }



    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        return in;
    }


    //----------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //----------------------------------------------------------------------------------------------

}
