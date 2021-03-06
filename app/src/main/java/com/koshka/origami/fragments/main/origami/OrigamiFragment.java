package com.koshka.origami.fragments.main.origami;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sprayart.SprayArt;
import com.koshka.origami.R;
import com.koshka.origami.helpers.fragment.OrigamiFragmentHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by imuntean on 7/20/16.
 */
public class OrigamiFragment extends Fragment {

    private static final String TAG = "OrigamiFragment";

    @BindView(R.id.origami_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.origamiImage)
    ImageView origamiImage;

    private OrigamiFragmentHelper fragmentHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.origami_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        fragmentHelper = new OrigamiFragmentHelper(getActivity(), slidingUpPanelLayout);
        origamiImage.setOnClickListener(view1 -> {
            Log.e("OrigamiImage","click");
            Intent intent = new Intent(getActivity(), SprayArt.class);
            startActivity(intent);
        });
    }



}
