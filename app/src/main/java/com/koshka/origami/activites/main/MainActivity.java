package com.koshka.origami.activites.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.data.network.api.APICommunication;
import com.example.data.network.api.RetrofitAPI;
import com.example.data.network.models.ApiResponse;
import com.example.data.network.models.response.Tag;
import com.example.data.network.models.response.UserMe;
import com.example.data.network.util.AddCookiesInterceptor;
import com.example.data.network.util.ReceivedCookiesInterceptor;
import com.example.data.network.util.RequestExecutor;
import com.facebook.FacebookSdk;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koshka.origami.R;
import com.koshka.origami.activites.OrigamiActivity;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.adapters.fragment.MainFragmentPagerAdapter;
import com.koshka.origami.fragments.main.map.OrigamiMapFragment;
import com.koshka.origami.helpers.activity.MainActivityHelper;
import com.koshka.origami.utils.ui.theme.OrigamiThemeHelper;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by imuntean on 7/20/16.
 */
public class MainActivity extends OrigamiActivity {

    private final static String TAG = "MainActivity";

    //---------------------------------------------------------------------------------------------

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_view_pager)
    ViewPager mPager;

    @BindView(R.id.smart_pager_tab_layout)
    SmartTabLayout mSmartTab;

    //----------------------------------------------------------------------------------------------

    private MainActivityHelper mainActivityHelper;

    public static Activity activity;

    private OrigamiMapFragment mapFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        activity = this;

        FacebookSdk.sdkInitialize(getApplicationContext());

        mainActivityHelper = new MainActivityHelper(this);

        mainActivityHelper.fragmentSetup(mPager, mSmartTab);
        mainActivityHelper.toolbarSetup(mToolbar);

        super.checkFirebase(this);


        APICommunication api = APICommunication.Companion.getInstance(this, null, this::getCookie);
        Observable<ApiResponse<List<Tag>>> tagObservable = api.getTags();
        APICommunication.Companion.execute(tagObservable, this::onTagsReceived, this::onTagsReceiveError);

        MainFragmentPagerAdapter mainFragmentPagerAdapter = (MainFragmentPagerAdapter) mainActivityHelper.getActivityHelper().getPagerAdapter();
        mapFragment = mainFragmentPagerAdapter.getMapFragment();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainActivityHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mainActivityHelper.buildMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mainActivityHelper.onOptionsItemSelected(item);
        return true;
    }

    //util method for intent creation from other activities
    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public void finishActivity() {
        this.finish();
    }

    public String getCookie() {
        SharedPreferences preferences = getSharedPreferences("com.koshka.origami", Activity.MODE_PRIVATE);
        return preferences.getString("Token", "");
    }

    public void onTagsReceived(ApiResponse<List<Tag>> tags) {
        for(Tag tag: tags.getData()) {
            Log.e("Tag", tag.getId() + tag.getDescription());
            mapFragment.getMap().addMarker(new MarkerOptions()
                    .position(new LatLng(tag.getLat(), tag.getLong()))
                    .title(tag.getDescription()));
        }
    }

    public void onTagsReceiveError(Throwable error) {
        error.printStackTrace();
    }

}


