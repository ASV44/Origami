package com.firebase.ui.auth.util.ui;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.R;

import java.util.Map;

/**
 * Created by hackintosh on 3/10/17.
 */

public class CustomAnimationListener implements Animation.AnimationListener{
    private Activity activity;
    private RelativeLayout login_layout;
    private RelativeLayout relativeLayout;
    private RelativeLayout alternative_login;
    private RelativeLayout register;
    private ImageView image;
    private RelativeLayout logo_layout;
    private RelativeLayout layout;
    private Map<Integer,Integer> rules;

    //public CustomAnimationListener(RelativeLayout layout, Map<Integer,Integer> rules) {
    public CustomAnimationListener(Activity activity) {
        super();
//        this.layout = layout;
//        this.rules = rules;
        this.activity = activity;
        login_layout = (RelativeLayout) activity.findViewById(R.id.login_layout);
        alternative_login = (RelativeLayout) activity.findViewById(R.id.alternative_login);
        register = (RelativeLayout) activity.findViewById(R.id.register_layout);
        relativeLayout = (RelativeLayout) activity.findViewById(R.id.relativeLayout);
        login_layout = (RelativeLayout) activity.findViewById(R.id.login_layout);
        image = (ImageView) activity.findViewById(R.id.image);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        layout.clearAnimation();
        RelativeLayout.LayoutParams params = getNewParams(login_layout);
//        for (int key : rules.keySet()) {
//            params.addRule(key,rules.get(key));
//        }
//
//        layout.setLayoutParams(params);
        login_layout.clearAnimation();
        adjust_Login_layout();
        adjust_relativeLayout();
        adjust_alternativeLoginLayout();
        relativeLayout.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        alternative_login.setVisibility(View.VISIBLE);
        relativeLayout.animate().alpha(1.0f).setDuration(1000);
        register.animate().alpha(1.0f).setDuration(1000);
        alternative_login.animate().alpha(1.0f).setDuration(1000);
        image.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public RelativeLayout.LayoutParams getNewParams(RelativeLayout layout) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layout.getWidth(),
                                                                             layout.getHeight());
        return params;
    }

    public void adjust_Login_layout() {
        RelativeLayout.LayoutParams params = getNewParams(login_layout);
        params.addRule(RelativeLayout.ALIGN_BOTTOM,0);
        params.addRule(RelativeLayout.CENTER_VERTICAL,1);
        login_layout.setLayoutParams(params);
    }

    public void adjust_relativeLayout() {
        RelativeLayout.LayoutParams params = getNewParams(relativeLayout);
        params.addRule(RelativeLayout.BELOW,0);
        params.addRule(RelativeLayout.ABOVE,R.id.login_layout);
        relativeLayout.setLayoutParams(params);
    }

    public void adjust_logoLayout() {
        RelativeLayout.LayoutParams params = getNewParams(login_layout);
        params.addRule(RelativeLayout.ABOVE,R.id.register_layout);
        login_layout.setLayoutParams(params);
    }

    public void adjust_alternativeLoginLayout() {
        RelativeLayout.LayoutParams params = getNewParams(alternative_login);
        params.addRule(RelativeLayout.ABOVE,0);
        params.addRule(RelativeLayout.BELOW,R.id.login_layout);
        alternative_login.setLayoutParams(params);
    }
}
