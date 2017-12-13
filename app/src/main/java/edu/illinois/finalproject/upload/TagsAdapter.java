package edu.illinois.finalproject.upload;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.illinois.finalproject.R;

/**
 * Created by Brandon on 12/4/17.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> tags;
    private List<String> clickedTags;
    public static final int MAX_SUGGESTIONS = 8;
    private Context context;
    private boolean defaultClicked;

    public TagsAdapter(Context context, boolean defaultClicked) {
        this.context = context;
        this.clickedTags =  new ArrayList<>();
        this.tags = new ArrayList<>();
        this.defaultClicked = defaultClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        private boolean clicked;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tag_view);
            clicked = false;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickTag();
                }
            });
            if (defaultClicked) {
                clicked = !clicked;
                textView.setTextColor(context.getColor(R.color.colorAccent));
                clickedTags.add(tags.get(tags.size() - 1));
            }
        }

        public void clickTag() {
            String tag = tags.get(getPosition());
            clicked = !clicked;
            if (clicked) {
                textView.setTextColor(context.getColor(R.color.colorAccent));
                if (!clickedTags.contains(tag)) {
                    clickedTags.add(tag);
                }
            } else {
                textView.setTextColor(context.getColor(R.color.colorPrimaryDark));
                if (clickedTags.contains(tag)) {
                    clickedTags.remove(tag);
                }
            }
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
        return Math.min(tags.size(), MAX_SUGGESTIONS);
    }

    /**
     * @param tag
     */
    public void addTags(String tag) {
        if (tag == null) {
            return;
        }
        this.tags.add(tag);
    }

    public List<String> getClickedTags() {
        return clickedTags;
    }
}
