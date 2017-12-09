package edu.illinois.finalproject.map;

import android.content.Context;
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

    private Context context;
    private List<String> infoTags;

    public MapInfoTagsAdapter(Context context, List<String> tags) {
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
    public MapInfoTagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_card, parent, false);

        return new MapInfoTagsAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(MapInfoTagsAdapter.ViewHolder holder, int position) {
        holder.tagView.setText(infoTags.get(position));
        holder.tagView.setTextSize(context.getResources().getDimension(R.dimen.map_info_tag_size));
    }

    @Override
    public int getItemCount() {
        return infoTags.size();
    }
}
