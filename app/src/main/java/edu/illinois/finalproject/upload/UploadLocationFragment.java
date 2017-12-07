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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    private GoogleMap gMap;
    private Bitmap displayBitmap;
    public static final float INFO_WINDOW_OFFSET = 0.1f;

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

        displayBitmap = Bitmap.createScaledBitmap(capturedBitmap, capturedBitmap.getWidth() / 2, capturedBitmap.getHeight() / 2, false);

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
                LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

                MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context, displayBitmap);
                gMap.setInfoWindowAdapter(mapMarkerAdapter);

                double angle = 0.0;
                float x = (float) (Math.sin(-angle * Math.PI / 180) / 2f + 0.5);
                float y = (float) (-(Math.cos(-angle * Math.PI / 180) / 2f - 0.5)) - INFO_WINDOW_OFFSET;

                Marker currentMarker = gMap.addMarker(new MarkerOptions().position(location));
                currentMarker.setInfoWindowAnchor(x, y);
                currentMarker.showInfoWindow();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // need to implement but no functionality needed
    }
}
