package com.example.logic.utils;

import com.example.logic.constants.Seasons;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by qm0937 on 10/6/16.
 */

public class TimeUtil {

    private static Calendar calendar = Calendar.getInstance();

    public static int getHourOfDay() {

        calendar.setTimeZone(TimeZone.getDefault());

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        return hourOfDay;
    }

    public static int getDate() {

        calendar.setTimeZone(TimeZone.getDefault());
        int date = calendar.get(Calendar.DAY_OF_MONTH);

        return date;
    }

    public static int getDayOfWeek() {

        calendar.setTimeZone(TimeZone.getDefault());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek;
    }

    public static int getDayOfYear() {

        calendar.setTimeZone(TimeZone.getDefault());
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        return dayOfYear;
    }

    public static int getMonth(){
        calendar.setTimeZone(TimeZone.getDefault());
        int month = calendar.get(Calendar.MONTH);

        return month;
    }

    public static Seasons getSeason(){
        int month = getMonth();

        if (month >= 0 && month <=2){
            return Seasons.AUTUMN;
        } else if (month>2 && month <4){
            return Seasons.SPRING;
        } else if (month>4 && month<6){
            return Seasons.SUMMER;
        } else
            return Seasons.WINTER;

    }


}
