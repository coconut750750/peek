package edu.illinois.finalproject.map;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.illinois.finalproject.firebase.Picture;

import java.util.HashMap;

import edu.illinois.finalproject.R;

/**
 *
 */
public class MapFragment extends Fragment {
    public static final String PHOTOS_REF = "photo_ids";
    private StorageReference rootStorage;
    private DatabaseReference photoIdRef;


    private MapView mapView;
    private MapManager mapManager;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootStorage = FirebaseStorage.getInstance().getReference();
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
                        HashMap<String, Double> location = uploadedPicture.getCoord();
                        final LatLng uploadedLocation = new LatLng(location.get(Picture.LATITUDE), location.get(Picture.LONGITUDE));

                        new FirebaseStorageAsync(mapManager, uploadedLocation).execute(uploadedPicture);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // need to implement but no functionality needed
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map);

        mapManager = new MapManager(getContext(), mapView, null);
        mapManager.startMap(savedInstanceState);

        //toolbar_upload
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return view;
    }
}
