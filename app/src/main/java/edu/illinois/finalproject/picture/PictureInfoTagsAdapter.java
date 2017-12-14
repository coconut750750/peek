package edu.illinois.finalproject.picture;

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
 * This adapter is used to load the list of tags to the detailed picture view (which is displayed
 * on the MapFragment, on the ProfileFragment, and on the ConfirmLocationFragment)
 */

public class PictureInfoTagsAdapter extends
        RecyclerView.Adapter<PictureInfoTagsAdapter.ViewHolder> {

    private List<String> infoTags;

    public PictureInfoTagsAdapter(List<String> tags) {
        this.infoTags = tags;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tagView;

        public ViewHolder(View itemView) {
            super(itemView);
            tagView = (TextView) itemView.findViewById(R.id.tag_view);
        }
    }

    /**
     * When the view is created, return a new ViewHolder with the view of the tag_info_map_card
     *
     * @param parent   passed by the Android System
     * @param viewType passed by the Android System; this parameter is ignored
     * @return a new ViewHolder with the layout for the tag
     */
    @Override
    public PictureInfoTagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_info_map_card, parent, false);

        return new PictureInfoTagsAdapter.ViewHolder(cardView);
    }

    /**
     * When the ViewHolder binds, set the TextView to the tag from the list of InfoTags.
     *
     * @param holder   the ViewHolder that is being binded
     * @param position the position of the ViewHolder
     */
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
