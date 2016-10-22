package com.koshka.origami.helpers.fragment;

import android.app.Activity;

import com.koshka.origami.helpers.slidingbar.OrigamiSlidingPanelHelper;
import com.koshka.origami.helpers.slidingbar.SlidingUpPanelHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by qm0937 on 10/22/16.
 */

public class OrigamiFragmentHelper {

    private Activity activity;

    private FragmentHelper fragmentHelper;
    private OrigamiSlidingPanelHelper slidingPanelHelper;


    public OrigamiFragmentHelper(Activity activity, SlidingUpPanelLayout layout) {
        this.activity = activity;

        fragmentHelper = new FragmentHelper(activity);
        slidingPanelHelper = new OrigamiSlidingPanelHelper(activity, layout);
    }
}
