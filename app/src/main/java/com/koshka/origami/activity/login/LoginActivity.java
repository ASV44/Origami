package com.koshka.origami.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.koshka.origami.R;
import com.koshka.origami.activity.main.MainActivity;
import com.koshka.origami.adapter.fragment.LoginFragmentPagerAdapter;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;
import com.koshka.origami.utils.net.NetworkUtil;
import com.koshka.origami.utils.ui.ParallaxPagerTransformer;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by imuntean on 7/19/16.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";
    private static final int RC_SIGN_IN = 100;

    private int backButtonCount;
    private Locale myLocale;

    @BindView(android.R.id.content)
    View mRootView;

    @BindView(R.id.login_pager)
    ViewPager mPager;

    @BindView(R.id.sign_in)
    Button mSignIn;

    @BindView(R.id.title_text)
    TextView titleTextView;

    private TypedArray day_background_array;
    private TypedArray night_background_array;

    private  int random_background_drawable_int;

    private boolean isNight = false;

    private OrigamiThemeHelper helper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(MainActivity.createIntent(this));
            finish();
        }

        beforeViews();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));
        //TODO: PLEASE, EXPORT THOSE IN A CONFIG FILE, PROP FILE OR SOME CONST CLASS OR SOMETHING
        pt.setBorder(0);
        pt.setSpeed(0.7f);

        mPager.setPageTransformer(false, pt);
        mPager.setAdapter(new LoginFragmentPagerAdapter(getSupportFragmentManager()));

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/actonia.ttf");

        if (font != null){
            titleTextView.setTypeface(font);
        }


    }

    private void beforeViews(){
        helper = new OrigamiThemeHelper(this);
        helper.randomThemeSetAndSave();
    }

    private void afterBindingViews(){


    }

    @OnClick(R.id.sign_in)
    public void signIn(View view) {

        boolean isConnectedToNetwork = NetworkUtil.isNetworkConnected(this);

        if (isConnectedToNetwork) {
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

    @MainThread
    @StyleRes
    private int getSelectedTheme() {

        if (helper.getRandomPickedTheme() != -1){
            return helper.getRandomPickedTheme();
        } else {
            return R.style.amethist_theme;
        }
        
    }

    @MainThread
    @DrawableRes
    private int getSelectedLogo() {
        return AuthUI.NO_LOGO;
    }

    @MainThread
    private String[] getSelectedProviders() {

        ArrayList<String> selectedProviders = new ArrayList<>();
        selectedProviders.add(AuthUI.EMAIL_PROVIDER);
        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @MainThread
    private String getSelectedTosUrl() {
        return GOOGLE_TOS_URL;
    }



    @Override
    public void onBackPressed() {

        //TODO: ON DOUBLE BACK PRESSED ISN't WORKING PROPERLY, FIND ANOTHER SOLUTION...
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

 /*   @OnClick(R.id.language_text_button)
    public void changeLanguageButton(final View view) {

        //TODO: CHANGE LANGUAGE IS TOO HARDCODED, NEEDS REFACTOR, LOOP THE LANGUAGES ARRAY
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_language)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                setLocale("en");
                                break;
                            case 1:
                                setLocale("ru");
                                break;
                            case 2:
                                setLocale("fr");
                                break;
                            case 3:
                                setLocale("de");
                                break;
                            case 4:
                                setLocale("es");
                                break;
                            case 5:
                                setLocale("ro");
                                break;
                            default:
                                setLocale("en");
                                break;
                        }
                    }
                });
        builder.show();
    }

    public void setLocale(String lang) {
        //TODO: conf.locale is deprecated, find another solution...
        //pressing multiple times crashes the app
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
    }
*/
    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @MainThread
    private void showShortSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_SHORT).show();
    }


    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, LoginActivity.class);
        return in;
    }


}
