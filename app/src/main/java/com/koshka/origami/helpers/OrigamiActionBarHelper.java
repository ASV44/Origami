package com.koshka.origami.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.koshka.origami.R;

/**
 * Created by qm0937 on 10/4/16.
 */

public class OrigamiActionBarHelper{



    public static void setMainActivityAttributes(Activity activity, ActionBar actionBar){
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
            actionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater inflator = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.action_bar_title, null);

            TextView titleTV = (TextView) v.findViewById(R.id.title_main);
            Typeface font = Typeface.createFromAsset(activity.getAssets(),
                    "fonts/actonia.ttf");
            titleTV.setTypeface(font);
            actionBar.setCustomView(v);

        }

    }


}
