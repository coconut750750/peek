package edu.illinois.finalproject.upload;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapManager;

/**
 *
 */
public class UploadLocationFragment extends Fragment {

    private MapManager mapManager;
    private MapView mapView;

    public UploadLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_location, container, false);

        mapView = (MapView) view.findViewById(R.id.map);
        mapManager = new MapManager(getContext(), mapView, new ArrayList<LatLng>());
        mapManager.startMap(savedInstanceState);

        return view;
    }
}
