package br.com.pedrosilva.tecnonutri.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.ProgressBar
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.FeedItemClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FeedUserAdapter(
    private val feedItemClickListener: FeedItemClickListener
) : GenericAdapter<FeedItem, RecyclerView.ViewHolder>(true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(R.layout.item_feed_user, parent, false)
            ViewHolder(view, feedItemClickListener)
        } else {
            val view = inflater.inflate(R.layout.progress, parent, false)
            ViewHolderLoader(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            val item = getItem(position)
            (holder as? ViewHolder)?.setup(item)
        } else {
            (holder as? GenericAdapter<*, *>.ViewHolderLoader)
                ?.setup(WRAP_CONTENT, WRAP_CONTENT)
        }
    }

    internal inner class ViewHolder(
        view: View,
        private val feedItemClickListener: FeedItemClickListener
    ) : RecyclerView.ViewHolder(view) {

        private val ivMeal: ImageView = view.findViewById(R.id.iv_meal)
        private val pbLoadingImage: ProgressBar = view.findViewById(R.id.pb_loading_image)

        fun setup(feedItem: FeedItem) {
            pbLoadingImage.visibility = View.VISIBLE
            ivMeal.setImageDrawable(null)
            itemView.setSingleOnClickListener {
                feedItemClickListener.invoke(feedItem)
            }
            Picasso.get()
                .load(feedItem.imageUrl)
                .fit()
                .centerCrop()
                .into(ivMeal, object : Callback {
                    override fun onSuccess() {
                        pbLoadingImage.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {
                        // do nothing
                    }
                })
        }
    }
}