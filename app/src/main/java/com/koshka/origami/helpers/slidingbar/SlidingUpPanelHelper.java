package com.koshka.origami.helpers.slidingbar;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.koshka.origami.utils.KeyboardUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by qm0937 on 10/22/16.
 */

public class SlidingUpPanelHelper  {

    private SlidingUpPanelLayout slidingUpPanelLayout;

    public SlidingUpPanelHelper(SlidingUpPanelLayout slidingUpPanelLayout) {
        this.slidingUpPanelLayout = slidingUpPanelLayout;
    }

    public void collapse(){
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public void anchor(float anchorPoint){
        slidingUpPanelLayout.setAnchorPoint(anchorPoint);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    public void expand(){
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void hide(){
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    public void changeHeight(int height){
        slidingUpPanelLayout.setPanelHeight(height);
    }

    public void addListener(SlidingUpPanelLayout.PanelSlideListener listener){
        slidingUpPanelLayout.addPanelSlideListener(listener);
    }
    public void removeListener(SlidingUpPanelLayout.PanelSlideListener listener){
        slidingUpPanelLayout.removePanelSlideListener(listener);
    }

    public SlidingUpPanelLayout getPanel(){
        return slidingUpPanelLayout;
    }

}
