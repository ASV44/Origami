package com.koshka.origami.model;

/**
 * Created by qm0937 on 10/3/16.
 */

public class UserPreferences {

    private int backgroundColor;
    private String shownName;



    public String getShownName() {
        return shownName;
    }

    public void setShownName(String shownName) {
        this.shownName = shownName;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
