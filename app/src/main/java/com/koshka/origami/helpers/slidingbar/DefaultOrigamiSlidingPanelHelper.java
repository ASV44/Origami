package com.koshka.origami.helpers.slidingbar;

import android.view.View;

/**
 * Created by qm0937 on 10/22/16.
 */

public class DefaultOrigamiSlidingPanelHelper {

    private SlidingUpPanelHelper slidingUpPanelHelper;

    private View view1;
    private View view2;
    private View view3;


    public DefaultOrigamiSlidingPanelHelper(SlidingUpPanelHelper slidingUpPanelHelper, View view1, View view2, View view3) {
        this.slidingUpPanelHelper = slidingUpPanelHelper;
        this.view1 = view1;
        this.view2 = view2;
        this.view3 = view3;
    }
}
