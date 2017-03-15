package com.koshka.origami.fragments.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.koshka.origami.R;
import com.koshka.origami.activites.login.LoginActivity;
import com.koshka.origami.helpers.fragment.FacebookLogInHelper;

import java.util.List;


/**
 * Created by imuntean on 7/19/16.
 */
public class FirstPageFragment3 extends Fragment {

    private LoginActivity activity;
    private FacebookLogInHelper facebookLogInHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FirstPageFragment3","onCreateView_start");
        return inflater.inflate(R.layout.login_third_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("FirstPageFragment3","onViewCreated_start");
        super.onViewCreated(view, savedInstanceState);
        facebookLogInHelper = new FacebookLogInHelper(activity,this);
    }

    public void setActivity(Activity activity) { this.activity = (LoginActivity) activity; }

    public FacebookLogInHelper getFacebookLogInHelper() { return this.facebookLogInHelper; }
}
