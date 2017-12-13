package edu.illinois.finalproject.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.picture.Picture;
import edu.illinois.finalproject.picture.PictureParser;

/**
 * Created by Brandon on 12/4/17.
 * <p>
 * This Adapter controls how an InfoWindow of a MapMarker should look like. It has two hashmaps.
 * This first on, the pictureHashMap maps the Id of a marker to the Picture Object that should
 * be displayed for that Marker. The second, the viewHashMap maps the Id of a marker to the view
 * that should be displayed. The view of each marker is saved so that when a marker is clicked on
 * again, the view, specifically the bitmap from the Picture URI, can be immediately loaded without
 * having the user to wait for the bitmap to be downloading Asynchronously.
 * <p>
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private HashMap<String, Picture> pictureHashMap;
    private HashMap<String, View> viewHashMap;

    /**
     * Instantiates the two hashmaps and gets the Context, which is used to get an layoutInflater
     *
     * @param context the context of the Activity/Fragment using the adapter
     */
    public MapMarkerAdapter(Context context) {
        this.context = context;
        this.pictureHashMap = new HashMap<>();
        this.viewHashMap = new HashMap<>();
    }

    /**
     * This is called when a marker is clicked. The adapter creates a view for that adapter based
     * on the Picture corresponding to the Marker Id. It uses the PictureParser object to insert
     * all of the Picture's details into the layout.
     *
     * @param marker the marker that was clicked
     * @return the completed view to be displayed
     */
    @Override
    public View getInfoWindow(Marker marker) {
        String id = marker.getId();

        if (viewHashMap.containsKey(id)) {
            return viewHashMap.get(id);
        } else {
            Picture markerPicture = pictureHashMap.get(id);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.single_post_info_card, null);

            View completeView = PictureParser.insertPicInfo(markerPicture, v);

            viewHashMap.put(id, completeView);

            return completeView;
        }

    }

    /**
     * This method is only called if getInfoWindow() returns null. Since it does not, this method
     * returns null. No functionality is needed other than that.
     * @return null
     */
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * Adds a picture to the pictureHashMap along with the marker id.
     * @param id String id of the marker that corresponds with the Picture
     * @param picture the Picture to display
     */
    public void addPicture(String id, Picture picture) {
        pictureHashMap.put(id, picture);
    }
}
