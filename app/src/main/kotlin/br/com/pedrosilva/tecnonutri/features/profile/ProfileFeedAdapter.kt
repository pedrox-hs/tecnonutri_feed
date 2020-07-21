package br.com.pedrosilva.tecnonutri.features.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.features.common.view.GenericAdapter
import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.features.common.listener.FeedItemClickListener
import br.com.pedrosilva.tecnonutri.features.common.listener.setSingleOnClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_feed_user.view.iv_meal
import kotlinx.android.synthetic.main.item_feed_user.view.pb_loading_image

class ProfileFeedAdapter(
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

        fun setup(feedItem: FeedItem) = itemView.run {
            pb_loading_image.visibility = View.VISIBLE
            iv_meal.setImageDrawable(null)
            itemView.setSingleOnClickListener {
                feedItemClickListener.invoke(feedItem)
            }
            Picasso.get()
                .load(feedItem.imageUrl)
                .fit()
                .centerCrop()
                .into(iv_meal, object : Callback {
                    override fun onSuccess() {
                        pb_loading_image.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {
                        // do nothing
                    }
                })
        }
    }
}