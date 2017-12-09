package edu.illinois.finalproject.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.firebase.Picture;

/**
 * Created by Brandon on 12/4/17.
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    public static final double DISPLAY_BITMAP_SCALE = 1 / 3f;
    private Context context;
    private HashMap<String, Picture> pictureHashMap;

    public MapMarkerAdapter(Context context, HashMap<String, Picture> pictureHashMap) {
        this.context = context;
        this.pictureHashMap = pictureHashMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_info, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.map_info_image);
        Picture markerPicture = pictureHashMap.get(marker.getId());

        Bitmap markerBitmap = markerPicture.getBitmap();
        int newWidth = (int) (markerBitmap.getWidth() * DISPLAY_BITMAP_SCALE);
        int newHeight = (int) (markerBitmap.getHeight() * DISPLAY_BITMAP_SCALE);
        Bitmap displayBitmap = Bitmap.createScaledBitmap(markerBitmap, newWidth, newHeight, false);

        imageView.setImageBitmap(displayBitmap);

        RecyclerView tagsRecycler = (RecyclerView) v.findViewById(R.id.map_info_tags_recycler);
        tagsRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        MapInfoTagsAdapter infoTagsAdapter = new MapInfoTagsAdapter(context, markerPicture.getTags());
        tagsRecycler.setAdapter(infoTagsAdapter);

        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
