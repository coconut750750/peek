package edu.illinois.finalproject.map;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Brandon on 12/3/17.
 */

public class MapManager {

    private MapView mapView;

    public MapManager(MapView mapView) {
        this.mapView = mapView;
    }
}
