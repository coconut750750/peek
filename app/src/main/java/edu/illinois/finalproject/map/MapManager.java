package edu.illinois.finalproject.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/***
 * Created by Brandon on 12/3/17.
 */

public class MapManager implements GoogleApiClient.ConnectionCallbacks {

    public static final float INFO_WINDOW_OFFSET = 0.1f;
    public static final float INFO_WINDOW_X = 0.5f;
    public static final float INFO_WINDOW_Y = -1 + INFO_WINDOW_OFFSET;

    private Context context;
    private MapView mapView;
    private GoogleApiClient googleApiClient;
    private GoogleMap gMap;
    private HashMap<Bitmap, LatLng> imageLocations;
    private HashMap<String, Bitmap> displayImages;

    public MapManager(Context context, MapView mapView, HashMap<Bitmap, LatLng> imageLocations) {
        this.context = context;
        this.mapView = mapView;
        if (imageLocations == null) {
            this.imageLocations = new HashMap<>();
        } else {
            this.imageLocations = imageLocations;
        }
        displayImages = new HashMap<>();

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
            double lat = lastLocation.getLatitude();
            double lon = lastLocation.getLongitude();

            // moves camera of the map to the location of the picture with zoom of DEFAULT_ZOOM
            // adds a marker to show where picture was taking
            // shows where the user is currently
            // https://developers.google.com/maps/documentation/android-api/map-with-marker
            gMap.setMyLocationEnabled(true); // displays current location
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), DEFAULT_ZOOM));
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
        // https://stackoverflow.com/questions/16536414/how-to-use-mapview-in-android-
        // using-google-map-v2
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
    public void displayBitmap(Bitmap bitmap) {
        if (imageLocations.containsKey(bitmap)) {
            Marker currentMarker = gMap.addMarker(new MarkerOptions().position(imageLocations.get(bitmap)));
            currentMarker.setInfoWindowAnchor(INFO_WINDOW_X, INFO_WINDOW_Y);

            displayImages.put(currentMarker.getId(), bitmap);

            MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context, displayImages);
            gMap.setInfoWindowAdapter(mapMarkerAdapter);

            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return false;
                }
            });
        }
    }

    public void addBitmap(Bitmap bitmap, LatLng location) {
        this.imageLocations.put(bitmap, location);
    }
}
