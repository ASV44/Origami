package com.example.logic.algorithms;

import android.content.res.TypedArray;

import com.example.logic.constants.Seasons;
import com.example.logic.random.ResourcePicker;
import com.example.logic.utils.TimeUtil;

/**
 * Created by qm0937 on 10/6/16.
 */

public class OrigamiThemePickAlgorithm  {

    private int[] dayThemeArray;
    private int[] nightThemeArray;

    private int hourOfDay;
    private int dayOfYear;
    private boolean isNight;


    //TODO: Set themes according to the time zone, season and time.
    // In Europe for winter set winter themes and time to darker themes to earlier
    //...and so on
    //..for now it's just a random pick
    //ALSO IT WOULD BE GOOD TO CHECK THE MOBILE PHONE SENSORS AND DECIDE THE LUMINOZITY
    public static int pickTheme(TypedArray dayThemeArray, TypedArray nightThemeArray){

        int hourofday = TimeUtil.getHourOfDay();
        int month = TimeUtil.getMonth();
        Seasons seasons = TimeUtil.getSeason();

        ResourcePicker picker = new ResourcePicker();
        int dayPickedTheme = picker.pick(dayThemeArray);
        int nightPickedTheme = picker.pick(nightThemeArray);

        switch (seasons){
            case SUMMER:{
                if ( hourofday > 21 || hourofday < 6) {
                    return nightPickedTheme;
                } else {
                    return dayPickedTheme;
                }

            }
            case WINTER:{
                if ( hourofday > 21 || hourofday < 6) {
                    return nightPickedTheme;
                } else {
                    return dayPickedTheme;
                }

            }
            case AUTUMN:{
                if ( hourofday > 21 || hourofday < 6) {
                    return nightPickedTheme;
                } else {
                    return dayPickedTheme;
                }


            }
            case SPRING:{
                if ( hourofday > 21 || hourofday < 6) {
                    return nightPickedTheme;
                } else {
                    return dayPickedTheme;
                }


            } default:{
                if ( hourofday > 21 || hourofday < 6) {
                    return nightPickedTheme;
                } else {
                    return dayPickedTheme;
                }


            }
        }


    }


}
