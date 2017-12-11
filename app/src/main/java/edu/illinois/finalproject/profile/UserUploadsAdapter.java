package edu.illinois.finalproject.profile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.firebase.Picture;

/***
 * Created by Brandon on 12/6/17.
 */

public class UserUploadsAdapter extends RecyclerView.Adapter<UserUploadsAdapter.ViewHolder> {

    private List<Picture> pictures;

    public UserUploadsAdapter() {
        this.pictures = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.info_image);
        }
    }

    @Override
    public UserUploadsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_info_card, parent, false);

        return new UserUploadsAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(UserUploadsAdapter.ViewHolder holder, int position) {
        ImageView imageView = holder.imageView;
        imageView.setImageBitmap(pictures.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void addImages(Picture downloadedPicture) {
        this.pictures.add(downloadedPicture);
    }
}
