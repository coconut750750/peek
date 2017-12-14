package edu.illinois.finalproject.upload;

import android.content.Context;
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
 * This is an Adapter class used to control the tags provided by Clarifai and the tags created by
 * the user themselves. It has a list of tags and a list of clickedTags.
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private List<String> tags;
    private List<String> clickedTags;
    private Context context;
    private boolean defaultClicked;

    /**
     * The constructor of the Adapter
     *
     * @param context        the context of the Activity using this adapter
     * @param defaultClicked the default state of a tag. If false, the tag will be black and wont
     *                       be added to the clickedTags list; otherwise, the tag will be accented
     *                       and will be added to the clickedTags list.
     */
    public TagsAdapter(Context context, boolean defaultClicked) {
        this.context = context;
        this.clickedTags = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.defaultClicked = defaultClicked;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        private boolean clicked;

        /**
         * Constructor of the tag ViewHolder. Gets the textView for the tag, sets the
         * onClickListener to the itemView. Then, if the adapter forces new tags to be clicked by
         * default, make this tag accented, and add it to the list of clickedTags
         *
         * @param itemView the view of a single tag
         */
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
                String lastTag = tags.get(tags.size() - 1);
                if (!clickedTags.contains(lastTag)) {
                    clickedTags.add(lastTag);
                }
            }
        }

        /**
         * When a tag is clicked, get the tag from the list of tags. If not already clicked, set
         * the color to the accent color and add the tag to the clickedTags list; otherwise, set
         * the color to black and remove the tag from the clickTags list.
         */
        public void clickTag() {
            String tag = tags.get(getAdapterPosition());
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

    /**
     * Sets the textView of the ViewHolder to the String tag
     *
     * @param holder   the ViewHolder being binded
     * @param position position of the Viewholder
     */
    @Override
    public void onBindViewHolder(TagsAdapter.ViewHolder holder, int position) {
        TextView textView = holder.textView;
        textView.setText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    /**
     * Adds a tag to the tags list if the tag is a nonnull tag not already in the list
     *
     * @param tag
     */
    public void addTags(String tag) {
        if (tag == null || tags.contains(tag)) {
            return;
        }
        tags.add(tag);
    }

    public List<String> getClickedTags() {
        return clickedTags;
    }
}
