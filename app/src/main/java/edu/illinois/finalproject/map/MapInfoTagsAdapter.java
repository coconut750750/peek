package edu.illinois.finalproject.map;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.upload.TagsAdapter;

/**
 * Created by Brandon on 12/9/17.
 */

public class MapInfoTagsAdapter extends RecyclerView.Adapter<MapInfoTagsAdapter.ViewHolder> {

    private List<String> infoTags;

    public MapInfoTagsAdapter(List<String> tags) {
        this.infoTags = tags;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tagView;

        public ViewHolder(View itemView) {
            super(itemView);

            tagView = (TextView) itemView.findViewWithTag(R.id.tag_view);
        }
    }

    @Override
    public MapInfoTagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_card, parent, false);

        return new MapInfoTagsAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(MapInfoTagsAdapter.ViewHolder holder, int position) {
        holder.tagView.setText(infoTags.get(position));
    }

    @Override
    public int getItemCount() {
        return infoTags.size();
    }
}
