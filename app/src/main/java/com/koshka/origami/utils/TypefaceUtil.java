package com.koshka.origami.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by qm0937 on 10/2/16.
 */

public class TypefaceUtil {

    public static Typeface getTypeFace(Context context, String typeface){

        return Typeface.createFromAsset(context.getAssets(), typeface);
    }
}
