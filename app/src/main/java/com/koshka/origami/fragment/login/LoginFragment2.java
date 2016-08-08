package com.koshka.origami.fragment.login;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.koshka.origami.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/19/16.
 */
public class LoginFragment2 extends Fragment {

    @BindView(R.id.text_login2)
    TextView text;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/origamibats.ttf");

        text.setTypeface(font);

    }



}