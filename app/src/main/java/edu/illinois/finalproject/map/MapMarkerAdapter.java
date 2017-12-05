package edu.illinois.finalproject.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/4/17.
 * Source: https://developers.google.com/maps/documentation/android-api/infowindows
 */

public class MapMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    public static final String CURRENT_PIC = "current";
    private Context context;
    private Bitmap uploadImage;

    public MapMarkerAdapter(Context context) {
        this.context = context;
        this.uploadImage = null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.map_info, null);

        ImageView imageView = (ImageView) v.findViewById(R.id.map_info_image);
        TextView textView = (TextView) v.findViewById(R.id.map_info_coord);

        String id = marker.getTitle();
        if (CURRENT_PIC.equals(id)) {
            //imageView.setImageBitmap(uploadImage);
        }

        textView.setText(marker.getPosition().latitude + ", " + marker.getPosition().longitude);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
