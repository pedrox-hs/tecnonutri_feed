package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import br.com.pedrosilva.tecnonutri.R
import com.pedrenrique.tecnonutri.data.repositories.FeedRepositoryImpl
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.impl.ThreadExecutor
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedItemPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.FeedItemPresenterImpl
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FoodAdapter
import br.com.pedrosilva.tecnonutri.presentation.ui.components.CircleTransformation
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import br.com.pedrosilva.tecnonutri.util.AppUtil
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_details.cb_like
import kotlinx.android.synthetic.main.activity_post_details.iv_meal
import kotlinx.android.synthetic.main.activity_post_details.iv_profile_image
import kotlinx.android.synthetic.main.activity_post_details.pb_loading_image
import kotlinx.android.synthetic.main.activity_post_details.rl_header
import kotlinx.android.synthetic.main.activity_post_details.rv_food
import kotlinx.android.synthetic.main.activity_post_details.swipe_refresh
import kotlinx.android.synthetic.main.activity_post_details.tv_profile_general_goal
import kotlinx.android.synthetic.main.activity_post_details.tv_profile_name
import java.util.Date

class PostDetailsActivity : BaseActivity(), FeedItemPresenter.View {

    companion object {

        private const val EXTRA_FEED_ITEM_HASH = "EXTRA_FEED_ITEM_HASH"
        private const val EXTRA_FEED_ITEM_DATE = "EXTRA_FEED_ITEM_DATE"

        fun getCallingIntent(context: Context, feedHash: String, date: Date): Intent {
            val intent = Intent(context, PostDetailsActivity::class.java)
            intent.putExtra(EXTRA_FEED_ITEM_HASH, feedHash)
            intent.putExtra(EXTRA_FEED_ITEM_DATE, date)
            return intent
        }
    }

    private val feedHash by lazy {
        intent.getStringExtra(EXTRA_FEED_ITEM_HASH) ?: ""
    }
    private val itemDate: Date by lazy {
        intent.getSerializableExtra(EXTRA_FEED_ITEM_DATE) as? Date ?: Date()
    }
    private var feedItem: FeedItem? = null
    private val profile: Profile?
        get() = feedItem?.profile

    private var feedItemPresenter: FeedItemPresenterImpl? = null
    private val foodAdapter: FoodAdapter by lazy { FoodAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bindProgress()
        showProgress()

        init()
    }

    override fun onResume() {
        super.onResume()
        cb_like.isChecked = feedItemPresenter?.isLiked(feedHash) ?: false
    }

    private fun init() {
        setupRecyclerView()

        val dateFormatted = AppUtil.formatDate(itemDate)
        title = getString(R.string.meal_date, dateFormatted)


        rl_header.setSingleOnClickListener {
            profile?.also { profile ->
                Navigator.navigateToProfile(this, profile.id, profile.name)
            }
        }

        swipe_refresh.setOnRefreshListener { feedItemPresenter?.reload() }

        cb_like.setOnClickListener { view ->
            feedItemPresenter?.changeLike(feedHash, (view as CheckBox).isChecked)
        }

        feedItemPresenter = FeedItemPresenterImpl(
            ThreadExecutor.instance,
            MainThreadImpl.instance,
            this,
            FeedRepositoryImpl(),
            feedHash
        )
    }

    override fun onLoadFeedItem(item: FeedItem) {
        feedItem = item

        Picasso.get()
            .load(item.profile.imageUrl)
            .placeholder(R.drawable.profile_image_placeholder)
            .error(R.drawable.profile_image_placeholder)
            .transform(CircleTransformation())
            .fit()
            .centerCrop()
            .into(iv_profile_image)

        Picasso.get()
            .load(item.imageUrl)
            .fit()
            .into(iv_meal, object : Callback {
                override fun onSuccess() {
                    pb_loading_image.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    // do nothing
                }
            })

        cb_like.isChecked = item.isLiked
        tv_profile_name.text = item.profile.name
        tv_profile_general_goal.text = item.profile.generalGoal
        foodAdapter.feedItem = feedItem

        swipe_refresh.isRefreshing = false
    }

    override fun onLoadFail(error: Throwable) {
        swipe_refresh.isRefreshing = false
        showError(getString(R.string.fail_to_load_try_again))
    }

    private fun setupRecyclerView() {
        rv_food.adapter = foodAdapter
    }

    override fun onChangeLike(feedHash: String, like: Boolean) {
    }

    override fun reloadAll() {
        feedItemPresenter?.load()
    }
}
