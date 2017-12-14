package edu.illinois.finalproject.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

import edu.illinois.finalproject.main.MainActivity;
import edu.illinois.finalproject.picture.Picture;

import static edu.illinois.finalproject.picture.Picture.LATITUDE;
import static edu.illinois.finalproject.picture.Picture.LONGITUDE;
import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/**
 * Created by Brandon on 12/3/17.
 * This is a class to Manage the MapView of any activity, but primarily the MapFragment. It creates
 * a GoogleApiClient to get the location and retrieve a Google Map object which will be displayed
 * on a MapView (passed by the activity/fragment that is using this object). It also manages the
 * locations and number of map markers that will be displayed. Finally, it also creates a
 * MapMarkerAdapter which controls the view of the InfoWindow that pops up when a marker is clicked.
 */

public class MapManager implements GoogleApiClient.ConnectionCallbacks {

    // these three variables are used to adjust where the info window will be displayed. currently,
    // the window is displayed directly above the marker with a small offset.
    private static final float INFO_WINDOW_OFFSET = 0.1f;
    private static final float INFO_WINDOW_X = 0.5f;
    private static final float INFO_WINDOW_Y = -0.5f + INFO_WINDOW_OFFSET;

    private MapView mapView;
    private GoogleApiClient googleApiClient;
    private GoogleMap gMap;

    private MapMarkerAdapter mapMarkerAdapter;

    private List<Picture> initialPictures = new ArrayList<>();

    /**
     * When creating a MapManager, a MapView is needed to display the Map on. This constructor will
     * call the constructor with only a mapView as a parameter. This constructor also takes in a
     * list of intialPictures. These Picture objects will be immediately added to the map and their
     * info windows will immediately be shown.
     *
     * @param mapView         the MapView to display the GoogleMap onto
     * @param initialPictures the list of pictures to display immediately when map is loaded
     */
    public MapManager(MapView mapView, List<Picture> initialPictures) {
        this(mapView);
        this.initialPictures = initialPictures;
    }

    /**
     * When creating a new MapManager, a MapView is needed to display the Map on. Also a context is
     * needed for the MapMarkerAdapter and the Google API Client. This can be retrieved from the
     * mapView object.
     *
     * @param mapView the MapView to display the GoogleMap onto
     */
    public MapManager(MapView mapView) {
        Context context = mapView.getContext();
        this.mapView = mapView;

        mapMarkerAdapter = new MapMarkerAdapter(context);

        // create a google api client to access location
        // source: https://stackoverflow.com/questions/38242917/how-can-i-get-the-current-location-
        // android-studio
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /**
     * When the GoogleApiClient is connected, get the last known location. Then, display that
     * location and move the camera to that location.
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (MainActivity.coarseLocationPermission || MainActivity.fineLocationPermission) {
            // this red line can be ignored because I do check that permissions are granted
            // by using the static variables from the MainActivity
            Location lastLoc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if (lastLoc != null) {
                double lat = lastLoc.getLatitude();
                double lon = lastLoc.getLongitude();

                HashMap<String, Double> latLngMap = new HashMap<>();
                latLngMap.put(LATITUDE, lat);
                latLngMap.put(LONGITUDE, lon);

                LatLng currentLatLng = new LatLng(lat, lon);

                // moves camera of the map to the location of the picture with zoom of DEFAULT_ZOOM
                // https://developers.google.com/maps/documentation/android-api/map-with-marker
                gMap.setMyLocationEnabled(true); // displays current location
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));

                for (Picture picture : initialPictures) {
                    picture.setCoord(latLngMap);
                    Marker pictureMarker = displayPicture(picture);
                    pictureMarker.showInfoWindow();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // need to implement but no functionality needed
    }

    /**
     * Sets up the MapView and sets the Google Map object. Configured map settings to a default
     * map type. Sets up the InfoWindowAdapter for the GoogleMap. Finally, sets an
     * OnMarkerClickListener (which gives the app full control of how to react when a marker is
     * clicked).
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
                gMap.setInfoWindowAdapter(mapMarkerAdapter);

                gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    private Marker lastOpenned = null;

                    /**
                     * Called when a Marker is clicked. It will do the exact same thing as the
                     * default ClickListener, except that the GoogleMap camera will not
                     * focus onto the marker, which was usually not helpful to users.
                     *
                     * Source: https://stackoverflow.com/questions/15925319/how-to-disable-android
                     * -map-marker-click-auto-center
                     * @param marker the marker that was clicked
                     * @return always true to signal that the event was processed
                     */
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (lastOpenned != null) {
                            lastOpenned.hideInfoWindow();

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
     * Takes in a Picture object and displays it on the map by creating a new Marker at the location
     * of the Picture and then adding the Picture along with the marker ID to the MarkerAdapter.
     *
     * @param picture the Picture object to display.
     */
    public Marker displayPicture(Picture picture) {
        LatLng location = new LatLng(picture.getCoord().get(LATITUDE),
                picture.getCoord().get(LONGITUDE));

        Marker currentMarker = gMap.addMarker(new MarkerOptions().position(location));
        currentMarker.setInfoWindowAnchor(INFO_WINDOW_X, INFO_WINDOW_Y);

        mapMarkerAdapter.addPicture(currentMarker.getId(), picture);

        return currentMarker;
    }
}
