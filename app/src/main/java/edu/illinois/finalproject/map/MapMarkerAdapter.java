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
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private HashMap<String, Picture> pictureHashMap;
    private HashMap<String, View> viewHashMap;

    public MapMarkerAdapter(Context context, HashMap<String, Picture> pictureHashMap) {
        this.context = context;
        this.pictureHashMap = pictureHashMap;
        this.viewHashMap = new HashMap<>();
    }

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

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
