package edu.illinois.finalproject.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import edu.illinois.finalproject.R;

/**
 *
 */
public class MapFragment extends Fragment {

    private MapView mapView;
    private MapManager mapManager;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);

        mapManager = new MapManager(getContext(), mapView, new ArrayList<LatLng>());
        mapManager.startMap(savedInstanceState);

        //toolbar_upload
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }
}
