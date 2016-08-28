package com.koshka.origami.google_maps;

import android.graphics.drawable.Icon;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by imuntean on 8/28/16.
 */
public class OrigamiMarker implements ClusterItem{

    private final LatLng mPosition;
    private BitmapDescriptor origamiIcon;
    private String origamiTitle;

    public OrigamiMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public LatLng getmPosition() {
        return mPosition;
    }


    public String getOrigamiTitle() {
        return origamiTitle;
    }

    public void setOrigamiTitle(String origamiTitle) {
        this.origamiTitle = origamiTitle;
    }

    public BitmapDescriptor getOrigamiIcon() {
        return origamiIcon;
    }

    public void setOrigamiIcon(BitmapDescriptor origamiIcon) {
        this.origamiIcon = origamiIcon;
    }
}