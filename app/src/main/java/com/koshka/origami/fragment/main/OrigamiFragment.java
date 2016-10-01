package com.koshka.origami.fragment.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koshka.origami.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by imuntean on 7/20/16.
 */
public class OrigamiFragment extends Fragment {

    private static final String TAG = "OrigamiFragment";

    @BindView(R.id.origami_create_button)
    TextView origamiCreateButton;

    @BindView(R.id.origami_type_one_button)
    TextView origamiTypeOneButton;

    @BindView(R.id.origami_type_three_button)
    TextView origamiTypeThreeButton;


    @BindView(R.id.origami_sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origami, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/origamibats.ttf");

        if (font != null){
            origamiCreateButton.setTypeface(font);
            origamiTypeOneButton.setTypeface(font);
            origamiTypeThreeButton.setTypeface(font);
        }

        setSlidingUpPanel();

    }


    private void setSlidingUpPanel() {
/*
        slidingUpPanelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < v.getWidth()/2) {
                    slidingUpPanelLayout.setAnchorPoint(0.5f);
                } else if (event.getX() >v.getWidth()/2){
                    slidingUpPanelLayout.setAnchorPoint(0.9f);
                }
                return false;
            }
        });
        */

        slidingUpPanelLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                slidingUpPanelLayout.setAnchorPoint(0.5f);
                return false;
            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    slidingUpPanelLayout.setAnchorPoint(1f);
                    origamiTypeOneButton.setVisibility(View.INVISIBLE);
                    origamiTypeThreeButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        origamiCreateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                slidingUpPanelLayout.setAnchorPoint(1f);
                origamiTypeOneButton.animate().alpha(0.5f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        origamiTypeOneButton.setVisibility(View.VISIBLE);
                        super.onAnimationEnd(animation);
                    }
                });
                origamiTypeThreeButton.animate().alpha(0.5f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        origamiTypeThreeButton.setVisibility(View.VISIBLE);
                    }
                });
                return false;
            }
        });

    }

}
