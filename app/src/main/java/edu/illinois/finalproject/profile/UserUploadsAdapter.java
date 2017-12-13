package edu.illinois.finalproject.profile;

import android.content.Context;
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

/***
 * Created by Brandon on 12/6/17.
 */

public class UserUploadsAdapter extends RecyclerView.Adapter<UserUploadsAdapter.ViewHolder> {

    private List<Picture> pictures;
    private Context context;

    public UserUploadsAdapter(Context context) {
        this.pictures = new ArrayList<>();
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
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
        PictureParser.insertPicInfo(pictures.get(position), holder.itemView);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public void addImages(Picture downloadedPicture) {
        this.pictures.add(downloadedPicture);
    }
}
