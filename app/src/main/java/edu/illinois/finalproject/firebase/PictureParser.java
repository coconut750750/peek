package edu.illinois.finalproject.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.map.MapInfoTagsAdapter;

/**
 * Created by Brandon on 12/10/17.
 */

public class PictureParser {

    private static final String DATETIME_SEPARATOR = "_";
    private static final float DISPLAY_BITMAP_SCALE = 0.5f;

    public static View insertPicInfo(Picture completePicture, View pictureInfoView) {
        if (completePicture.getBitmap() == null) {
            return null;
        }
        insertPicName(completePicture.getName(), pictureInfoView);

        insertPicDatetime(completePicture.getDatetime(), pictureInfoView);

        insertPicImage(completePicture.getBitmap(), pictureInfoView);

        insertPicTags(completePicture.getTags(), pictureInfoView);

        return pictureInfoView;
    }

    private static void insertPicName(String name, View pictureInfoView) {
        TextView nameText = (TextView) pictureInfoView.findViewById(R.id.uploader_name);
        nameText.setText(name);
    }

    private static void insertPicDatetime(String datetime, View pictureInfoView) {
        // set datetime
        String[] datetimeArr = datetime.split(DATETIME_SEPARATOR);
        String date = datetimeArr[0];
        TextView dateText = (TextView) pictureInfoView.findViewById(R.id.date);
        dateText.setText(date);
        String time = datetimeArr[1];
        TextView timeText = (TextView) pictureInfoView.findViewById(R.id.time);
        timeText.setText(time);
    }

    private static void insertPicImage(Bitmap bitmap, View pictureInfoView) {
        // set image
        ImageView imageView = (ImageView) pictureInfoView.findViewById(R.id.info_image);
        Bitmap markerBitmap = bitmap;
        int newWidth = (int) (markerBitmap.getWidth() * DISPLAY_BITMAP_SCALE);
        int newHeight = (int) (markerBitmap.getHeight() * DISPLAY_BITMAP_SCALE);
        Bitmap displayBitmap = Bitmap.createScaledBitmap(markerBitmap, newWidth, newHeight, false);
        imageView.setImageBitmap(displayBitmap);
    }

    private static void insertPicTags(List<String> tags, View pictureInfoView) {
        // set tags
        Context context = pictureInfoView.getContext();
        RecyclerView tagsRecycler = (RecyclerView) pictureInfoView.findViewById(R.id.info_tags_recycler);
        tagsRecycler.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
        MapInfoTagsAdapter infoTagsAdapter = new MapInfoTagsAdapter(context, tags);
        tagsRecycler.setAdapter(infoTagsAdapter);
    }

//    if (markerPicture.getName() == null) {
//        v = inflater.inflate(R.layout.map_info_incomplete, null);
//    } else {
//        v = inflater.inflate(R.layout.single_post_info_card, null);
//
//
//    }
//

//


}