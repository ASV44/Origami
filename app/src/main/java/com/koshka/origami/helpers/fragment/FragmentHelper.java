package com.koshka.origami.helpers.fragment;

import android.app.Activity;

/**
 * Created by qm0937 on 10/21/16.
 */

public class FragmentHelper {

    private Activity activity;

    public FragmentHelper(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
