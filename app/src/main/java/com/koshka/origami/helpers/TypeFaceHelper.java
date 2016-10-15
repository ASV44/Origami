package com.koshka.origami.helpers;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by qm0937 on 10/15/16.
 */

public class TypeFaceHelper {

    private static final String TAG = "TypeFaceHelper";

    private final static String resPath = "fonts/";

    public final static String origamiLogoTypeFace = "origamibats.ttf";

    public static void setTypeFace(Activity activity, TextView textView, String typeface) {

        if (textView != null && textView.getText() != "" && !textView.getText().equals("")) {

            if (typeface != "" && !typeface.equals("")) {

                final Typeface font = Typeface.createFromAsset(activity.getAssets(), resPath + typeface);
                textView.setTypeface(font);

            } else {
                Log.d(TAG, "TypeFace is null");
            }


        }

    }

}
