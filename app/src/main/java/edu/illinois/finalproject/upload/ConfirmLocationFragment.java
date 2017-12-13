package edu.illinois.finalproject.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.picture.Picture;
import edu.illinois.finalproject.main.MainActivity;
import edu.illinois.finalproject.map.MapMarkerAdapter;

import static edu.illinois.finalproject.upload.UploadActivity.DEFAULT_ZOOM;

/**
 *
 */
public class ConfirmLocationFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks {

    public static final String NAME_KEY = "name";
    public static final String DATETIME_KEY = "datetime";

    private MapView mapView;
    private Context context;
    private GoogleApiClient googleApiClient;
    private GoogleMap gMap;
    private Bitmap capturedBitmap;
    public static final float INFO_WINDOW_OFFSET = 0.1f;
    public static final float INFO_WINDOW_X = 0.5f;
    public static final float INFO_WINDOW_Y = -1 + INFO_WINDOW_OFFSET;

    public ConfirmLocationFragment() {
        // Required empty public constructor
    }

    public static ConfirmLocationFragment newInstance(String name, String datetime) {
        Bundle args = new Bundle();
        args.putString(NAME_KEY, name);
        args.putString(DATETIME_KEY, datetime);

        ConfirmLocationFragment fragment = new ConfirmLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();

        googleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();
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
        if (MainActivity.fineLocationPermission || MainActivity.coarseLocationPermission) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

                addMarker(location);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // need to implement but no functionality needed
    }

    private void addMarker(LatLng location) {
        Marker currentMarker = gMap.addMarker(new MarkerOptions().position(location));
        currentMarker.setInfoWindowAnchor(INFO_WINDOW_X, INFO_WINDOW_Y);

        List<String> tags = ((UploadActivity)getActivity()).getSelectedTags();
        String name = getArguments().getString(NAME_KEY);
        String datetime = getArguments().getString(DATETIME_KEY);

        Picture displayPicture = new Picture(capturedBitmap, tags, name, datetime);

        HashMap<String, Picture> mapMarkerPictures = new HashMap<>();
        mapMarkerPictures.put(currentMarker.getId(), displayPicture);

        MapMarkerAdapter mapMarkerAdapter = new MapMarkerAdapter(context, mapMarkerPictures);
        gMap.setInfoWindowAdapter(mapMarkerAdapter);

        currentMarker.showInfoWindow();
    }
}
