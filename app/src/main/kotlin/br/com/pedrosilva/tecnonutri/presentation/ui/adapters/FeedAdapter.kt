package br.com.pedrosilva.tecnonutri.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile
import br.com.pedrosilva.tecnonutri.presentation.ui.components.CircleTransformation
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ChangeLikeListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.FeedItemClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ProfileClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener
import br.com.pedrosilva.tecnonutri.util.AppUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class FeedAdapter : GenericAdapter<FeedItem, RecyclerView.ViewHolder>(true) {

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

        private val tvProfileName: TextView = view.findViewById(R.id.tv_profile_name)
        private val ivProfileImage: ImageView = view.findViewById(R.id.iv_profile_image)
        private val tvProfileGeneralGoal: TextView = view.findViewById(R.id.tv_profile_general_goal)
        private val ivMeal: ImageView = view.findViewById(R.id.iv_meal)
        private val pbLoadingImage: ProgressBar = view.findViewById(R.id.pb_loading_image)
        private val tvMealDate: TextView = view.findViewById(R.id.tv_meal_date)
        private val tvEnergy: TextView = view.findViewById(R.id.tv_energy)
        private val rlHeader: RelativeLayout = view.findViewById(R.id.rl_header)
        private val cbLike: CheckBox = view.findViewById(R.id.cb_like)

        fun bindElements(feedItem: FeedItem) {
            bindEvents(feedItem)

            val profile = feedItem.profile
            val resources = itemView.context.resources
            val dateFormatted = AppUtil.formatDate(feedItem.date)

            ivProfileImage.setImageResource(R.drawable.profile_image_placeholder)
            ivMeal.setImageDrawable(null)
            pbLoadingImage.visibility = View.VISIBLE
            cbLike.isChecked = feedItem.isLiked
            tvProfileName.text = profile.name
            tvProfileGeneralGoal.text = profile.generalGoal
            tvMealDate.text = resources.getString(R.string.meal_date, dateFormatted)
            tvEnergy.text = resources.getString(R.string.qty_energy, AppUtil.formatKcal(feedItem.energy!!))

            loadImages(profile, feedItem)
        }

        private fun bindEvents(feedItem: FeedItem) {
            cbLike.setOnCheckedChangeListener { _, checked ->
                changeLikeListener?.invoke(feedItem.id, checked)
            }
            itemView.setSingleOnClickListener {
                feedItemClickListener?.invoke(feedItem)
            }
            rlHeader.setSingleOnClickListener {
                profileClickListener?.invoke(feedItem.profile)
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
                .into(ivProfileImage)

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