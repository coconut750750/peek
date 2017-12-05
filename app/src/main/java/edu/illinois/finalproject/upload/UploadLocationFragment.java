package edu.illinois.finalproject.upload;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
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
import com.google.android.gms.maps.model.MarkerOptions;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapMarkerAdapter;

import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/**
 *
 */
public class UploadLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks {

    private MapView mapView;
    private Context context;
    private GoogleApiClient googleApiClient;
    private double lat;
    private double lon;
    private GoogleMap gMap;
    private Bitmap displayBitmap;

    public UploadLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

        googleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        Bitmap capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();

        displayBitmap = Bitmap.createScaledBitmap(capturedBitmap, capturedBitmap.getWidth()/5, capturedBitmap.getHeight()/5, false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_location, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
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

        return view;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int granted = PackageManager.PERMISSION_GRANTED;

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == granted ||
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == granted) {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();

                gMap.setMyLocationEnabled(true); // displays current location
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), DEFAULT_ZOOM));

                MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context, displayBitmap);
                gMap.setInfoWindowAdapter(mapMarkerAdapter);

                double angle = 0.0;
                double x = Math.sin(-angle * Math.PI / 180) * 0.5 + 0.5;
                double y = -(Math.cos(-angle * Math.PI / 180) * 0.5 - 0.5);

                gMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).infoWindowAnchor((float) x, (float) y - 0.1f));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // need to implement but no functionality needed
    }
}
