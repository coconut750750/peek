package edu.illinois.finalproject.firebase;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

/***
 * Created by Brandon on 12/3/17.
 */

public class Picture {

    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lon";

    public String id;
    public String storageLocation;
    public HashMap<String, Double> coord;
    public List<String> tags;
    public String name;
    public String datetime;

    public Picture(String storageLocation, LatLng coord, List<String> tags, String name,
                   String datetime) {
        this.storageLocation = storageLocation;
        this.tags = tags;
        this.name = name;
        this.datetime = datetime;
        this.coord = new HashMap<>();
        this.coord.put(LATITUDE, coord.latitude);
        this.coord.put(LONGITUDE, coord.longitude);
    }

    public Picture() {
        // need this to retrieve Picture from firebase
    }

    public String getId() {
        return id;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public HashMap<String, Double> getCoord() {
        return coord;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public String getDatetime() {
        return datetime;
    }
}
