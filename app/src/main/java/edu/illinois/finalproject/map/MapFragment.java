package edu.illinois.finalproject.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import edu.illinois.finalproject.picture.Picture;

import java.util.HashMap;

import edu.illinois.finalproject.R;

/**
 * This is the Map Fragments that will be displayed by the MainActivity. It displays a map with
 * MapMarkers indicating where recent pictures have been taken.
 */
public class MapFragment extends Fragment {
    // the Firebase node of where all the photos ids are stored
    public static final String PHOTOS_REF = "photo_ids";
    private DatabaseReference photoIdRef;

    private MapView mapView;
    private MapManager mapManager;

    /**
     * This empty constructor is required by the Android System
     */
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * When the view is created, this Fragment sets up the toolbar, then instantiates a MapManager
     * Object to take care of displaying Picture Objects on the MapView, and finally sets a
     * ValueEventListener to the Database Reference to the picture Ids such that when the app
     * retrieves Picture Objects, the listener gets MapManager to display the Picture.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);

        // set the toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // instantiate a MapManager object
        mapManager = new MapManager(getContext(), mapView);
        mapManager.startMap(savedInstanceState);

        // get Picture objects form Firebase, and then add them to the MapManager
        photoIdRef = FirebaseDatabase.getInstance().getReference(PHOTOS_REF);
        photoIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Picture>> t = new GenericTypeIndicator<HashMap<String, Picture>>() {
                };
                HashMap<String, Picture> list = dataSnapshot.getValue(t);
                if (list != null) {
                    for (String id : list.keySet()) {
                        Picture uploadedPicture = list.get(id);

                        mapManager.displayPicture(uploadedPicture);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement but no functionality needed
            }
        });

        return view;
    }
}
