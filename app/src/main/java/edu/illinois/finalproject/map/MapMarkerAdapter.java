package edu.illinois.finalproject.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/4/17.
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private HashMap<String, Bitmap> bitmapMap;

    public MapMarkerAdapter(Context context, HashMap<String, Bitmap> bitmapMap) {
        this.context = context;
        this.bitmapMap = bitmapMap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_info, null);

//        LatLng markerPos = marker.getPosition();
//
//        TextView textView = (TextView) v.findViewById(R.i.map_info_coord);
//        textView.setText(String.format("%s, %s", markerPos.latitude, markerPos.longitude));

        ImageView imageView = (ImageView) v.findViewById(R.id.map_info_image);
        imageView.setImageBitmap(bitmapMap.get(marker.getId()));
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
