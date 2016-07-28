package com.koshka.origami.model;

/**
 * Created by imuntean on 7/26/16.
 */
public class SimpleTextOrigami {
    private String text;
    private String placeId;

    public SimpleTextOrigami() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
