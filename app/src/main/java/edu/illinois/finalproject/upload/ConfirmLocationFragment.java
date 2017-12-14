package edu.illinois.finalproject.upload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapManager;
import edu.illinois.finalproject.picture.Picture;

/**
 * The second Fragment to be displayed in the Upload Activity. It displays the Picture Object that
 * they have created on the map to confirm that everything on it is correct. This method uses a
 * MapManager to display the single Picture Object being uploaded.
 */
public class ConfirmLocationFragment extends Fragment {

    // keys used in the Bundle that can be retrieved from getArguments()
    public static final String NAME_KEY = "name";
    public static final String DATETIME_KEY = "datetime";

    private MapView mapView;
    private Bitmap capturedBitmap;

    private MapManager mapManager;

    public ConfirmLocationFragment() {
        // Required empty public constructor
    }

    /**
     * When creating a new ConfirmLocationFragment, a name and a datetime is needed to create a
     * Picture Object. These arguments are sent through the Upload activity and will be used
     * to create an object to be displayed on the map.
     *
     * @param name     name of uploader (which will be the current user)
     * @param datetime the current time
     * @return a ConfirmLocationFragment that the activity can use
     */
    public static ConfirmLocationFragment newInstance(String name, String datetime) {
        Bundle args = new Bundle();
        args.putString(NAME_KEY, name);
        args.putString(DATETIME_KEY, datetime);

        ConfirmLocationFragment fragment = new ConfirmLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * When this Fragment created, it retrieves the capturedBitmap from the UploadActivity.
     *
     * @param savedInstanceState passed by the Android system
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        capturedBitmap = ((UploadActivity) getActivity()).getCapturedBitmap();
    }

    /**
     * When this Fragment's View is being created, inflate the layout, retrieve the mapview,
     * and instantiate a MapManager to display the upload Picture.
     *
     * @param inflater           passed by the Android system, used to inflate the fragment layout
     * @param container          passed by the Android system
     * @param savedInstanceState passed by the Android system
     * @return the view of the Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_location,
                container, false);

        mapView = (MapView) view.findViewById(R.id.map);

        List<Picture> displayPics = new ArrayList<>();
        displayPics.add(getDisplayPicture());
        mapManager = new MapManager(mapView, displayPics);
        mapManager.startMap(savedInstanceState);

        return view;
    }

    /**
     * Creates a temporary Picture Object that the mapManager can display to the map
     *
     * @return an incomplete Picture Object constructed from the arguments passed by the
     * UploadActivity
     */
    public Picture getDisplayPicture() {
        List<String> tags = ((UploadActivity) getActivity()).getSelectedTags();
        String name = getArguments().getString(NAME_KEY);
        String datetime = getArguments().getString(DATETIME_KEY);

        return new Picture(capturedBitmap, tags, name, datetime);
    }
}
