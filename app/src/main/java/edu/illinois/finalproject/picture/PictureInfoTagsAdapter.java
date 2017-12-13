package edu.illinois.finalproject.picture;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/9/17.
 */

public class PictureInfoTagsAdapter extends RecyclerView.Adapter<PictureInfoTagsAdapter.ViewHolder> {

    private Context context;
    private List<String> infoTags;

    public PictureInfoTagsAdapter(Context context, List<String> tags) {
        this.context = context;
        this.infoTags = tags;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tagView;

        public ViewHolder(View itemView) {
            super(itemView);

            tagView = (TextView) itemView.findViewById(R.id.tag_view);
        }
    }

    @Override
    public PictureInfoTagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_info_map_card, parent, false);

        return new PictureInfoTagsAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(PictureInfoTagsAdapter.ViewHolder holder, int position) {
        holder.tagView.setText(infoTags.get(position));
    }

    @Override
    public int getItemCount() {
        if (infoTags == null) {
            return 0;
        }
        return infoTags.size();
    }
}
