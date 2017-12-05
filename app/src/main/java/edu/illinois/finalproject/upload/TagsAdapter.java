package edu.illinois.finalproject.upload;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/4/17.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> tags;
    public static final int MAX_SUGGESTIONS = 8;

    public TagsAdapter() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tag_view);
        }
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_card, parent, false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(TagsAdapter.ViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        if (tags == null) {
            return 0;
        }
        return Math.min(tags.size(), MAX_SUGGESTIONS);
    }

    /**
     *
     * @param tags
     */
    public void setTagsList(List<String> tags) {
        if (tags == null) {
            return;
        }
        this.tags = tags;
    }

    public boolean tagsRetrieved() {
        return tags != null;
    }
}
