package edu.illinois.finalproject.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import edu.illinois.finalproject.R;

import static edu.illinois.finalproject.camera.CapturedImageActivity.DEFAULT_ZOOM;

/**
 *
 */
public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks{

    private GoogleApiClient googleApiClient;
    private double lat;
    private double lon;
    private MapView mapView;
    private GoogleMap gMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a google api client to access location
        // source: https://stackoverflow.com/questions/38242917/how-can-i-get-the-current-location-
        // android-studio
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);

        startMap(savedInstanceState);

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
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
        }
    }

    /**
     * This method is called if the connection to the google api client is suspended. Error handling
     * is done in this method.
     */
    @Override
    public void onConnectionSuspended(int i) {
        // no implementation needed yet
    }

    /**
     * Sets up the mapView and sets the Google Map object. Configured map settings to a default
     * map type.
     *
     * @param savedInstanceState a bundle object the map view needs to create itself.
     */
    private void startMap(Bundle savedInstanceState) {
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
}
