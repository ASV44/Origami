package com.koshka.origami.helpers.slidingbar;

import android.app.Activity;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by qm0937 on 10/22/16.
 */

public class OrigamiSlidingPanelHelper {


    private Activity activity;
    private SlidingUpPanelHelper slidingUpPanelHelper;

    public OrigamiSlidingPanelHelper(Activity activity, SlidingUpPanelLayout layout) {
        this.activity = activity;

        slidingUpPanelHelper = new SlidingUpPanelHelper(layout);
    }
}
