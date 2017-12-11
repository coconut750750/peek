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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.illinois.finalproject.firebase.Picture;

import static edu.illinois.finalproject.firebase.Picture.LATITUDE;
import static edu.illinois.finalproject.firebase.Picture.LONGITUDE;
import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/***
 * Created by Brandon on 12/3/17.
 */

public class MapManager implements GoogleApiClient.ConnectionCallbacks {

    public static final float INFO_WINDOW_OFFSET = 0.1f;
    public static final float INFO_WINDOW_X = 0.5f;
    public static final float INFO_WINDOW_Y = -0.5f + INFO_WINDOW_OFFSET;

    private Context context;
    private MapView mapView;
    private GoogleApiClient googleApiClient;
    private GoogleMap gMap;
    private List<Picture> picturesOnMap;
    private HashMap<String, Picture> displayImages;

    public MapManager(Context context, MapView mapView, List<Picture> picturesOnMap) {
        this.context = context;
        this.mapView = mapView;
        if (picturesOnMap == null) {
            this.picturesOnMap = new ArrayList<>();
        } else {
            this.picturesOnMap = picturesOnMap;
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
                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    private Marker lastOpenned = null;

                    // https://stackoverflow.com/questions/15925319/how-to-disable-android-map-marker-click-auto-center
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Check if there is an open info window
                        if (lastOpenned != null) {
                            // Close the info window
                            lastOpenned.hideInfoWindow();

                            // If the marker the same marker that was already open
                            if (lastOpenned.equals(marker)) {
                                lastOpenned = null;
                                return true;
                            }
                        }

                        marker.showInfoWindow();
                        lastOpenned = marker;

                        return true;
                    }
                });
            }
        });
        mapView.onResume();
    }

    /**
     *
     */
    public void displayPicture(Picture picture) {
        if (picturesOnMap.contains(picture)) {
            LatLng location = new LatLng(picture.getCoord().get(LATITUDE),
                    picture.getCoord().get(LONGITUDE));

            Marker currentMarker = gMap.addMarker(new MarkerOptions().position(location));
            currentMarker.setInfoWindowAnchor(INFO_WINDOW_X, INFO_WINDOW_Y);

            displayImages.put(currentMarker.getId(), picture);

            MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context, displayImages);
            gMap.setInfoWindowAdapter(mapMarkerAdapter);
        }
    }

    public void addPicture(Picture picture) {
        this.picturesOnMap.add(picture);
    }
}
