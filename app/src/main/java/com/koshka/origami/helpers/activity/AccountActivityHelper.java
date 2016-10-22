package com.koshka.origami.helpers.activity;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by qm0937 on 10/18/16.
 */

public class AccountActivityHelper  {

    private static final String TAG = "AccountActivityHelper";

    private ActivityHelper activityHelper;

    private AVLoadingIndicatorView indicatorView;

    public AccountActivityHelper(ActivityHelper activityHelper) {
        this.activityHelper = activityHelper;
    }
}
