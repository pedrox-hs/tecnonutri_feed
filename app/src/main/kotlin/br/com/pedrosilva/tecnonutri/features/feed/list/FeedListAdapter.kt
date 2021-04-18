package br.com.pedrosilva.tecnonutri.features.feed.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.ext.format
import br.com.pedrosilva.tecnonutri.ext.formatKcal
import br.com.pedrosilva.tecnonutri.features.common.view.GenericAdapter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import br.com.pedrosilva.tecnonutri.features.common.widget.CircleTransformation
import br.com.pedrosilva.tecnonutri.features.common.listener.ChangeLikeListener
import br.com.pedrosilva.tecnonutri.features.common.listener.FeedItemClickListener
import br.com.pedrosilva.tecnonutri.features.common.listener.ProfileClickListener
import br.com.pedrosilva.tecnonutri.features.common.listener.setSingleOnClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_feed.view.cb_like
import kotlinx.android.synthetic.main.item_feed.view.iv_meal
import kotlinx.android.synthetic.main.item_feed.view.iv_profile_image
import kotlinx.android.synthetic.main.item_feed.view.pb_loading_image
import kotlinx.android.synthetic.main.item_feed.view.rl_header
import kotlinx.android.synthetic.main.item_feed.view.tv_energy
import kotlinx.android.synthetic.main.item_feed.view.tv_meal_date
import kotlinx.android.synthetic.main.item_feed.view.tv_profile_general_goal
import kotlinx.android.synthetic.main.item_feed.view.tv_profile_name

class FeedListAdapter : GenericAdapter<FeedItem, RecyclerView.ViewHolder>(true) {

    private var feedItemClickListener: FeedItemClickListener? = null
    private var profileClickListener: ProfileClickListener? = null
    private var changeLikeListener: ChangeLikeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = inflater.inflate(R.layout.item_feed, parent, false)
            ViewHolder(
                view,
                feedItemClickListener,
                profileClickListener,
                changeLikeListener
            )
        } else {
            val view = inflater.inflate(R.layout.progress, parent, false)
            ViewHolderLoader(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            val item = getItem(position)
            (holder as? ViewHolder)?.bindElements(item)
        } else {
            (holder as? GenericAdapter<*, *>.ViewHolderLoader)?.setup()
        }
    }

    fun setFeedItemClickListener(feedItemClickListener: FeedItemClickListener?) {
        this.feedItemClickListener = feedItemClickListener
    }

    fun setProfileClickListener(profileClickListener: ProfileClickListener?) {
        this.profileClickListener = profileClickListener
    }

    fun setChangeLikeListener(changeLikeListener: ChangeLikeListener?) {
        this.changeLikeListener = changeLikeListener
    }

    internal inner class ViewHolder(
        view: View,
        private val feedItemClickListener: FeedItemClickListener?,
        private val profileClickListener: ProfileClickListener?,
        private val changeLikeListener: ChangeLikeListener?
    ) : RecyclerView.ViewHolder(view) {

        fun bindElements(feedItem: FeedItem) = itemView.run {
            bindEvents(feedItem)

            val profile = feedItem.profile
            val resources = itemView.context.resources
            val dateFormatted = feedItem.date.format(context)

            iv_profile_image.setImageResource(R.drawable.profile_image_placeholder)
            iv_meal.setImageDrawable(null)
            pb_loading_image.visibility = View.VISIBLE
            cb_like.isChecked = feedItem.isLiked
            tv_profile_name.text = profile.name
            tv_profile_general_goal.text = profile.generalGoal
            tv_meal_date.text = resources.getString(R.string.meal_date, dateFormatted)
            tv_energy.text =
                resources.getString(R.string.qty_energy, feedItem.energy?.formatKcal().orEmpty())

            loadImages(profile, feedItem)
        }

        private fun bindEvents(feedItem: FeedItem) {
            itemView.cb_like.setOnCheckedChangeListener { _, checked ->
                changeLikeListener?.invoke(feedItem.id, checked)
            }
            itemView.rl_header.setSingleOnClickListener {
                profileClickListener?.invoke(feedItem.profile)
            }
            itemView.setSingleOnClickListener {
                feedItemClickListener?.invoke(feedItem)
            }
        }

        private fun loadImages(profile: Profile, feedItem: FeedItem) {
            Picasso.get()
                .load(profile.imageUrl)
                .placeholder(R.drawable.profile_image_placeholder)
                .error(R.drawable.profile_image_placeholder)
                .transform(CircleTransformation())
                .fit()
                .centerCrop()
                .into(itemView.iv_profile_image)

            Picasso.get()
                .load(feedItem.imageUrl)
                .fit()
                .centerCrop()
                .into(itemView.iv_meal, object : Callback {
                    override fun onSuccess() {
                        itemView.pb_loading_image.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {
                        // do nothing
                    }
                })
        }
    }
}