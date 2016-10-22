package com.koshka.origami.google.maps;

import android.graphics.drawable.Icon;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.koshka.origami.R;

/**
 * Created by imuntean on 8/28/16.
 */
public class OrigamiMarker implements ClusterItem{

    private final LatLng mPosition;
    private BitmapDescriptor origamiIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
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