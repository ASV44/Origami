package com.example.logic.random;

import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;

import java.util.Random;

/**
 * Created by qm0937 on 10/7/16.
 */

public class ResourcePicker {

    private static Random random = new Random();

    public int pick(TypedArray array) {
        int rnd = random.nextInt(array.length());
        return array.getResourceId(rnd, rnd);
    }

    public int pick(int index, TypedArray array){
        return array.getResourceId(index, 0);
    }
}
