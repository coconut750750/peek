package edu.illinois.finalproject.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/***
 * Created by Brandon on 12/3/17.
 */

public class MapManager implements GoogleApiClient.ConnectionCallbacks {

    private Context context;
    private MapView mapView;
    private GoogleApiClient googleApiClient;
    private double lat;
    private double lon;
    private GoogleMap gMap;
    private List<LatLng> mapMarkers;

    public MapManager(Context context, MapView mapView, List<LatLng> markers) {
        this.context = context;
        this.mapView = mapView;
        mapMarkers = markers;

        // create a google api client to access location
        // source: https://stackoverflow.com/questions/38242917/how-can-i-get-the-current-location-
        // android-studio
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permissions not granted so no map interaction
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();

            // moves camera of the map to the location of the picture with zoom of DEFAULT_ZOOM
            // adds a marker to show where picture was taking
            // shows where the user is currently
            // https://developers.google.com/maps/documentation/android-api/map-with-marker
            gMap.setMyLocationEnabled(true); // displays current location
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), DEFAULT_ZOOM));
            populateMap();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * Sets up the mapView and sets the Google Map object. Configured map settings to a default
     * map type.
     *
     * @param savedInstanceState a bundle object the map view needs to create itself.
     */
    public void startMap(Bundle savedInstanceState) {
        // starts and displays the map view
        // https://stackoverflow.com/questions/16536414/how-to-use-mapview-in-android-using-google-map-v2
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        mapView.onResume();
    }

    /**
     * Populates the map with a marker at the given latitude and longitude. The title of the marker
     * will be "photo" but this will be changed to show the actual photo.
     */
    private void populateMap() {
        MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context);
        gMap.setInfoWindowAdapter(mapMarkerAdapter);

        double angle = 0.0;
        double x = Math.sin(-angle * Math.PI / 180) * 0.5 + 0.5;
        double y = -(Math.cos(-angle * Math.PI / 180) * 0.5 - 0.5);

        for (LatLng photoCoord : mapMarkers) {
            gMap.addMarker(new MarkerOptions().position(photoCoord));
        }

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }
}
