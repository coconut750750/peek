package edu.illinois.finalproject.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/4/17.
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private Bitmap image;

    public MapMarkerAdapter(Context context, Bitmap image) {
        this.context = context;
        this.image = image;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.map_info, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.map_info_image);
        TextView textView = (TextView) v.findViewById(R.id.map_info_coord);

        textView.setText(marker.getPosition().latitude + ", " + marker.getPosition().longitude);

        imageView.setImageBitmap(image);
        marker.getId();
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
