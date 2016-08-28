package com.koshka.origami.google_maps;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
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
        markerOptions.icon(item.getOrigamiIcon());
        markerOptions.title(item.getOrigamiTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
