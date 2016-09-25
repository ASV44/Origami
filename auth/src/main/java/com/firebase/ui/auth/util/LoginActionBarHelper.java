package com.firebase.ui.auth.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by qm0937 on 9/25/16.
 */

public class LoginActionBarHelper {

    private static final boolean homeButtonEnabled = false;
    private static final boolean displayShowCustomEnabled = true;
    private static final boolean homeAsUpEnabled = false;
    private static final boolean showHomeEnabled = false;
    private static final boolean showTitleEnabled = false;

    @ColorRes
    private static final int defaultColor = R.color.white;

    @IdRes
    private static final int textViewId = R.layout.action_bar_text_layout;

    @ColorRes
    private int titleColorId;

    @IdRes
    private int toolbarId;

    private AppCompatActivity activity;
    private Context context;
    private Toolbar toolbar;
    private ActionBar actionBar;

    private String title;

    private LayoutInflater inflater;

    private TextView titleView;
    private AVLoadingIndicatorView indicatorView;


    public LoginActionBarHelper(AppCompatActivity activity, int toolbarId) {
        this.activity = activity;
        this.toolbarId = toolbarId;

        if (toolbarId != 0){
                toolbar = (Toolbar) activity.findViewById(toolbarId);
                activity.setSupportActionBar(toolbar);
        }
        actionBar = activity.getSupportActionBar();
        setUpActionBar();

         inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        titleView = (TextView) inflater.inflate(R.layout.action_bar_text_layout, null);

        //HARD
        Typeface font = Typeface.createFromAsset(activity.getAssets(),
                "fonts/actonia.ttf");
        titleView.setTypeface(font);
        indicatorView = (AVLoadingIndicatorView) inflater.inflate(R.layout.av_progress_indicator,null);

    }

    private void setUpActionBar(){

        if (actionBar != null){

            actionBar.setHomeButtonEnabled(homeButtonEnabled); // disable the button
            actionBar.setDisplayShowCustomEnabled(displayShowCustomEnabled);
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(showHomeEnabled); // remove the icon
            actionBar.setDisplayShowTitleEnabled(showTitleEnabled);
        }

        toolbar.setTitleTextColor(activity.getResources().getColor(defaultColor));

    }

    public void buildTitlePlusIndicatorActionBar(String title){

        hideIndicator();
        this.title = title;
        titleView.setText(title);

        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layout.height = 180;
        layout.width = 180;
        layout.gravity = Gravity.CENTER;

        toolbar.addView(indicatorView, layout);

        Toolbar.LayoutParams layoutForText = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layoutForText.gravity = Gravity.CENTER;

        if (titleColorId != 0){
            toolbar.setTitleTextColor(activity.getResources().getColor(titleColorId));
        } else {
            toolbar.setTitleTextColor(activity.getResources().getColor(defaultColor));
        }

        toolbar.addView(titleView, layoutForText);
    }

    public void buildTitleOnlyActionBar(String title){

        titleView.setText(title);

        Toolbar.LayoutParams layout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        layout.gravity = Gravity.CENTER;


        if (titleColorId != 0){
            toolbar.setTitleTextColor(activity.getResources().getColor(titleColorId));
        } else {
            toolbar.setTitleTextColor(activity.getResources().getColor(defaultColor));
        }

        toolbar.addView(titleView, layout);
    }

    public void showIndicator(){

        if(indicatorView != null){
            indicatorView.show();
        }
    }

    public void hideIndicator(){
        if (indicatorView != null){
            indicatorView.hide();
        }
    }
    public void smoothHideIndicator(){
        if (indicatorView != null){
            indicatorView.smoothToHide();
        }
    }
    public void smoothToShowIndicator(){
        if (indicatorView != null){
            indicatorView.smoothToShow();
        }
    }

    public void hideTitle(){
        if (titleView != null){
            titleView.setVisibility(View.GONE);
        }
    }

    public  void showTitle(){
        if (titleView != null){
            titleView.setVisibility(View.VISIBLE);
        }
    }

    public void showIndicatorHideTitle(){
        smoothToShowIndicator();
        hideTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleColorId() {
        return titleColorId;
    }

    public void setTitleColorId(int titleColorId) {
        this.titleColorId = titleColorId;
    }
}
