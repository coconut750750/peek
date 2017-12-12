package edu.illinois.finalproject.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.firebase.Picture;
import edu.illinois.finalproject.firebase.PictureParser;

/**
 * Created by Brandon on 12/4/17.
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private HashMap<String, Picture> pictureHashMap;

    public MapMarkerAdapter(Context context, HashMap<String, Picture> pictureHashMap) {
        this.context = context;
        this.pictureHashMap = pictureHashMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Picture markerPicture = pictureHashMap.get(marker.getId());

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.single_post_info_card, null);

        View completeView = PictureParser.insertPicInfo(markerPicture, v);

        return completeView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
