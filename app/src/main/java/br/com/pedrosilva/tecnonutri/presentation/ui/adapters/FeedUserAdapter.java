package br.com.pedrosilva.tecnonutri.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.FeedItemClickListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener;

/**
 * Created by psilva on 3/20/17.
 */

public class FeedUserAdapter extends GenericAdapter<FeedItem, RecyclerView.ViewHolder> {

    private final FeedItemClickListener feedItemClickListener;

    public FeedUserAdapter(Context context, FeedItemClickListener feedItemClickListener) {
        super(context, true);
        this.feedItemClickListener = feedItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEWTYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_feed_user, parent, false);
            viewHolder = new ViewHolder(view, feedItemClickListener);
        } else {
            View view = inflater.inflate(R.layout.progress, parent, false);
            viewHolder = new ViewHolderLoader(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEWTYPE_ITEM) {
            final FeedItem item = getItem(position);
            ((ViewHolder) holder).setup(item);
        } else {
            ((ViewHolderLoader) holder).setup(ViewHolderLoader.WRAP_CONTENT, ViewHolderLoader.WRAP_CONTENT);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivMeal;
        private ProgressBar pbLoadingImage;

        public ViewHolder(View v, FeedItemClickListener feedItemClickListener) {
            super(v);
            bindElements(v);
            v.setOnClickListener(new FeedItemClick(feedItemClickListener, this));
        }

        private void bindElements(View v) {
            ivMeal = (ImageView) v.findViewById(R.id.iv_meal);
            pbLoadingImage = (ProgressBar) v.findViewById(R.id.pb_loading_image);
        }

        public void setup(FeedItem feedItem) {
            final Context context = itemView.getContext();
            pbLoadingImage.setVisibility(View.VISIBLE);

            if (feedItem != null) {
                Picasso.with(context)
                        .load(feedItem.getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(ivMeal, new Callback() {
                            @Override
                            public void onSuccess() {
                                pbLoadingImage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                            }
                        });
            } else {
                ivMeal.setImageDrawable(null);
            }
        }
    }

    private class FeedItemClick extends SingleOnClickListener {

        private final ViewHolder vh;
        private final FeedItemClickListener listener;

        FeedItemClick(FeedItemClickListener listener, ViewHolder vh) {
            this.listener = listener;
            this.vh = vh;
        }

        @Override
        public void onItemClick(View view) {
            if (listener != null) {
                FeedItem feedItem = getItem(vh.getAdapterPosition());
                if (feedItem != null)
                    listener.onClickView(feedItem);
            }
        }
    }
}
