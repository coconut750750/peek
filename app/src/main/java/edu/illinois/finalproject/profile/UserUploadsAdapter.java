package edu.illinois.finalproject.profile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.picture.Picture;
import edu.illinois.finalproject.picture.PictureParser;

/**
 * Created by Brandon on 12/6/17.
 * This adapter is used by the ProfileFragment. It displays Picture Objects into the RecyclerView
 * of the ProfileFragment. It uses the PictureParser to put the Picture details into the individual
 * ViewHolders.
 */

public class UserUploadsAdapter extends RecyclerView.Adapter<UserUploadsAdapter.ViewHolder> {

    private List<Picture> pictures;

    public UserUploadsAdapter() {
        this.pictures = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
        }
    }

    /**
     * Creates and returns a new ViewHolder which contains the layout for the Picture details
     *
     * @param parent   passed by the Android system
     * @param viewType passed by the Android system
     * @return a new ViewHolder Object
     */
    @Override
    public UserUploadsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_info_card, parent, false);

        return new UserUploadsAdapter.ViewHolder(cardView);
    }

    /**
     * When the ViewHolder is binded, insert Picture info into the ViewHolder.
     *
     * @param holder   the ViewHolder being adjusted
     * @param position the position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(UserUploadsAdapter.ViewHolder holder, int position) {
        PictureParser.insertPicInfo(pictures.get(position), holder.itemView);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    /**
     * Adds a Picture Object to the list of pictures to display
     *
     * @param downloadedPicture the picture to add
     */
    public void addImages(Picture downloadedPicture) {
        this.pictures.add(downloadedPicture);
    }
}
