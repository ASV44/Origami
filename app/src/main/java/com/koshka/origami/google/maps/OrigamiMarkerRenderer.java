package com.koshka.origami.google.maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by imuntean on 8/28/16.
 */
public class OrigamiMarkerRenderer extends DefaultClusterRenderer<OrigamiMarker> {


    public OrigamiMarkerRenderer(Context context, GoogleMap map, ClusterManager<OrigamiMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(OrigamiMarker item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(item.getOrigamiIcon());

    }

    @Override
    protected void onClusterItemRendered(OrigamiMarker clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        if (clusterItem.getOrigamiTitle() != null){
            marker.setTitle(clusterItem.getOrigamiTitle());
        }
    }
}
