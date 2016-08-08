/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.firebase.ui.auth.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.TriangleSkewSpinIndicator;

import static com.firebase.ui.auth.util.Preconditions.checkNotNull;

public class ActivityHelper {
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private AVLoadingIndicatorView indicatorView;
    private final FlowParameters mFlowParams;

    public ActivityHelper(Activity activity, Intent intent) {
        mActivity = activity;
        mFlowParams = intent.getParcelableExtra(ExtraConstants.EXTRA_FLOW_PARAMS);
    }

    public void configureTheme() {
        mActivity.setTheme(mFlowParams.themeId);
    }

    public void dismissDialog() {
        if(indicatorView != null){
            indicatorView.hide();
            indicatorView = null;
        }

    }

    public FlowParameters getFlowParams() {
        return mFlowParams;
    }

    public void showLoadingDialog(String message) {
        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layout.gravity=Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        layout.height = 300;
        layout.width = 200;
        layout.gravity = Gravity.CENTER;
        View view = inflater.inflate(R.layout.av_progress_indicator,null);

        mActivity.addContentView(view,layout);
        indicatorView = (AVLoadingIndicatorView) mActivity.findViewById(R.id.av_progress_indicator);
        indicatorView.show();
    }

    public void showLoadingDialog(@StringRes int stringResource) {
        showLoadingDialog(mActivity.getString(stringResource));
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    public void finish(int resultCode, Intent intent) {
        mActivity.setResult(resultCode, intent);
        mActivity.finish();
    }

    public Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    public String getAppName() {
        return mFlowParams.appName;
    }

    public FirebaseApp getFirebaseApp() {
        return FirebaseApp.getInstance(mFlowParams.appName);
    }

    public FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance(getFirebaseApp());
    }

    public CredentialsApi getCredentialsApi() {
        return Auth.CredentialsApi;
    }

    public FirebaseUser getCurrentUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public static Intent createBaseIntent(
            @NonNull Context context,
            @NonNull Class<? extends Activity> target,
            @NonNull FlowParameters flowParams) {
        return new Intent(
                checkNotNull(context, "context cannot be null"),
                checkNotNull(target, "target activity cannot be null"))
                .putExtra(ExtraConstants.EXTRA_FLOW_PARAMS,
                        checkNotNull(flowParams, "flowParams cannot be null"));
    }
}
