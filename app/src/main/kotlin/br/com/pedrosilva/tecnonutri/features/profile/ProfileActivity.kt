package br.com.pedrosilva.tecnonutri.features.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.features.common.Navigator
import br.com.pedrosilva.tecnonutri.features.common.error.ErrorData
import br.com.pedrosilva.tecnonutri.features.common.listener.EndlessRecyclerViewScrollListener
import br.com.pedrosilva.tecnonutri.features.common.view.BaseActivity
import br.com.pedrosilva.tecnonutri.features.common.view.HasPresenter
import com.google.android.material.appbar.AppBarLayout
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.rv_feed_user
import kotlinx.android.synthetic.main.activity_profile.swipe_refresh
import kotlinx.android.synthetic.main.toolbar_profile.appbar
import kotlinx.android.synthetic.main.toolbar_profile.collapsing_toolbar
import kotlinx.android.synthetic.main.toolbar_profile.iv_profile_image
import kotlinx.android.synthetic.main.toolbar_profile.toolbar
import kotlinx.android.synthetic.main.toolbar_profile.tv_general_goal
import kotlinx.android.synthetic.main.toolbar_profile.tv_profile_name
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class ProfileActivity : BaseActivity(),
    ProfileContract.View,
    HasPresenter,
    AppBarLayout.OnOffsetChangedListener {

    companion object {

        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f

        private const val EXTRA_PROFILE_ID = "EXTRA_PROFILE_ID"
        private const val EXTRA_PROFILE_NAME = "EXTRA_PROFILE_NAME"

        fun getCallingIntent(context: Context, id: Int, name: String): Intent {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(EXTRA_PROFILE_ID, id)
            intent.putExtra(EXTRA_PROFILE_NAME, name)
            return intent
        }
    }

    @Inject
    override lateinit var presenter: ProfileContract.Presenter

    override val profileUserId by lazy { intent.getIntExtra(EXTRA_PROFILE_ID, 0) }
    private val title by lazy { intent.getStringExtra(EXTRA_PROFILE_NAME) ?: "" }
    private var timestamp = 0
    private var nextPage = 0

    private val profileFeedAdapter: ProfileFeedAdapter by lazy {
        ProfileFeedAdapter(
            ::onFeedItemClicked
        )
    }
    private val gridLayoutManager: GridLayoutManager by lazy {
        GridLayoutManager(this, 3)
    }
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private var isFirstLoad = true
    private var isTheTitleVisible = false
        set(value) {
            if (value != field) {
                field = value
                collapsing_toolbar.isTitleEnabled = value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupToolbar(title)
        setupRecyclerView()

        tv_profile_name.text = title
        swipe_refresh.setOnRefreshListener { presenter.refresh() }
    }

    private fun setupRecyclerView() {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (profileFeedAdapter.isListEnded || position < (profileFeedAdapter.itemCount - 1)) 1
                else 3
        }
        rv_feed_user.layoutManager = gridLayoutManager
        rv_feed_user.adapter = profileFeedAdapter

        endlessRecyclerViewScrollListener =
            EndlessRecyclerViewScrollListener(
                gridLayoutManager
            ) { _, _ ->
                if (!isFirstLoad) {
                    presenter.loadMore(nextPage, timestamp)
                }
            }
        profileFeedAdapter.setRetryClickListener {
            endlessRecyclerViewScrollListener.resetState()
            presenter.loadMore(nextPage, timestamp)
        }
    }

    private fun setupToolbar(title: String) {
        appbar.addOnOffsetChangedListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        collapsing_toolbar.title = title
        collapsing_toolbar.isTitleEnabled = false
        collapsing_toolbar.setExpandedTitleColor(
            ContextCompat.getColor(this, android.R.color.transparent)
        )
    }

    override fun onLoadProfile(
        profile: Profile,
        feedItems: List<FeedItem>,
        page: Int,
        timestamp: Int
    ) {
        this.timestamp = timestamp
        this.nextPage = page + 1
        if (isFirstLoad || swipe_refresh.isRefreshing) {
            isFirstLoad = false

            Picasso.get()
                .load(profile.imageUrl)
                .placeholder(R.drawable.profile_image_placeholder)
                .error(R.drawable.profile_image_placeholder)
                .fit()
                .centerCrop()
                .into(iv_profile_image)

            collapsing_toolbar.title = profile.name
            tv_profile_name.text = profile.name

            tv_general_goal!!.text = profile.generalGoal

            profileFeedAdapter.items = feedItems
            swipe_refresh.isRefreshing = false
            endlessRecyclerViewScrollListener.resetState()
            rv_feed_user.clearOnScrollListeners()
            rv_feed_user.addOnScrollListener(endlessRecyclerViewScrollListener)
        } else {
            profileFeedAdapter.appendItems(feedItems)
        }
        if (feedItems.count() == 0)
            profileFeedAdapter.notifyEndList()
    }

    override fun onLoadFail(error: Throwable) {
        swipe_refresh.isRefreshing = false
        val msgError = getString(R.string.fail_to_load_try_again)
        profileFeedAdapter.notifyError(msgError)
        presenter.onError(ErrorData(msgError))
    }

    private fun onFeedItemClicked(feedItem: FeedItem) {
        Navigator.navigateToFeedItemDetails(this, feedItem.id, feedItem.date)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll: Int = appBarLayout.totalScrollRange
        val percentage: Float = (abs(offset) / maxScroll.toFloat())

        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        isTheTitleVisible = percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR
    }
}
