package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.data.repositories.ProfileRepositoryImpl
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile
import br.com.pedrosilva.tecnonutri.domain.executor.impl.ThreadExecutor
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator
import br.com.pedrosilva.tecnonutri.presentation.presenters.ProfilePresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.ProfilePresenterImpl
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FeedUserAdapter
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.EndlessRecyclerViewScrollListener
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.rv_feed_user
import kotlinx.android.synthetic.main.activity_profile.swipe_refresh
import kotlinx.android.synthetic.main.toolbar_profile.appbar
import kotlinx.android.synthetic.main.toolbar_profile.collapsing_toolbar
import kotlinx.android.synthetic.main.toolbar_profile.iv_profile_image
import kotlinx.android.synthetic.main.toolbar_profile.toolbar
import kotlinx.android.synthetic.main.toolbar_profile.tv_general_goal
import kotlinx.android.synthetic.main.toolbar_profile.tv_profile_name
import kotlin.math.abs

class ProfileActivity : BaseActivity(), ProfilePresenter.View,
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

    private val userId by lazy { intent.getIntExtra(EXTRA_PROFILE_ID, 0) }
    private val title by lazy { intent.getStringExtra(EXTRA_PROFILE_NAME) ?: "" }
    private var timestamp = 0
    private var nextPage = 0

    private var profilePresenter: ProfilePresenter? = null
    private val feedUserAdapter: FeedUserAdapter by lazy {
        FeedUserAdapter(::onFeedItemClicked)
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
        swipe_refresh.setOnRefreshListener { profilePresenter?.refresh(userId) }

        profilePresenter = ProfilePresenterImpl(
            ThreadExecutor.instance,
            MainThreadImpl.instance,
            this,
            ProfileRepositoryImpl(),
            userId
        )
    }

    private fun setupRecyclerView() {
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                if (feedUserAdapter.isListEnded || position < (feedUserAdapter.itemCount - 1)) 1
                else 3
        }
        rv_feed_user.layoutManager = gridLayoutManager
        rv_feed_user.adapter = feedUserAdapter

        endlessRecyclerViewScrollListener =
            EndlessRecyclerViewScrollListener(gridLayoutManager) { _: Int, _: RecyclerView ->
                if (isFirstLoad) {
                    profilePresenter?.load(userId)
                } else {
                    profilePresenter?.loadMore(userId, nextPage, timestamp)
                }
            }
        feedUserAdapter.setRetryClickListener {
            endlessRecyclerViewScrollListener.resetState()
            profilePresenter?.loadMore(userId, nextPage, timestamp)
        }
    }

    private fun setupToolbar(title: String) {
        appbar.addOnOffsetChangedListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        collapsing_toolbar.title = title
        collapsing_toolbar.isTitleEnabled = false
        collapsing_toolbar.setExpandedTitleColor(getColor(this, android.R.color.transparent))
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

            feedUserAdapter.items = feedItems
            swipe_refresh.isRefreshing = false
            endlessRecyclerViewScrollListener.resetState()
            rv_feed_user.clearOnScrollListeners()
            rv_feed_user.addOnScrollListener(endlessRecyclerViewScrollListener)
        } else {
            feedUserAdapter.appendItems(feedItems)
        }
        if (feedItems.count() == 0)
            feedUserAdapter.notifyEndList()
    }

    override fun onLoadFail(error: Throwable) {
        swipe_refresh.isRefreshing = false
        val msgError = getString(R.string.fail_to_load_try_again)
        feedUserAdapter.notifyError(msgError)
        showError(msgError)
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
