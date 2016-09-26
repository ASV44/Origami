package com.koshka.origami.fragment.login;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koshka.origami.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.koshka.origami.R.id.textView;

/**
 * Created by imuntean on 7/19/16.
 */
public class FirstPageFragment2 extends Fragment {

    @BindView(R.id.text_login2)
    TextView text;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page_fragment2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/origamibats.ttf");

        Resources res = getResources();
/*
        Shader shader = new LinearGradient(0, 0, 0, text.getTextSize(), Color.RED, Color.BLUE,
                Shader.TileMode.CLAMP);

        text.getPaint().setShader(shader);*/

        text.setTypeface(font);

    }



}
