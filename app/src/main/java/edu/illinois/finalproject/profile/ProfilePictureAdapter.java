package edu.illinois.finalproject.profile;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/6/17.
 */

public class ProfilePictureAdapter extends RecyclerView.Adapter<ProfilePictureAdapter.ViewHolder> {

    private List<Bitmap> images;

    public ProfilePictureAdapter(List<Bitmap> images) {
        this.images = images;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.post_image);
        }
    }

    @Override
    public ProfilePictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_card, parent, false);

        return new ProfilePictureAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ProfilePictureAdapter.ViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        imageView.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
