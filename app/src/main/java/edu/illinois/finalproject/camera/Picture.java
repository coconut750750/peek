package edu.illinois.finalproject.camera;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brandon on 12/3/17.
 */

public class Picture {
    public String storage_location;
    public List<Double> coord;
    public List<String> tags;
    public String name;
    public String datetime;

    public Picture(String storageLocation, LatLng coord, List<String> tags, String name, String datetime) {
        this.storage_location = storageLocation;
        this.tags = tags;
        this.name = name;
        this.datetime = datetime;
        this.coord = new ArrayList<>();
        this.coord.add(coord.latitude);
        this.coord.add(coord.longitude);
    }

    public Picture() {
        // need this to retrieve Picture from firebase
    }

    public String getStorageLocation() {
        return storage_location;
    }

    public LatLng getLocation() {
        LatLng photoCoord = new LatLng(coord.get(0), coord.get(1));
        return photoCoord;
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
