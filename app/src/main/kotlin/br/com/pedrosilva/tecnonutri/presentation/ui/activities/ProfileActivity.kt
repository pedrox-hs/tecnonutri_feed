package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
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

    private var userId = 0
    private var title = ""
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

    private var swipeRefresh: SwipeRefreshLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var appBar: AppBarLayout? = null
    private var toolbar: Toolbar? = null

    private var ivProfileImage: ImageView? = null
    private var tvProfileName: TextView? = null
    private var tvGeneralGoal: TextView? = null
    private var rvFeedUser: RecyclerView? = null

    private var isFirstLoad = true
    private var isTheTitleVisible = false
        set(value) {
            if (value != field) {
                field = value
                collapsingToolbarLayout?.isTitleEnabled = value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bindElements()
        init()
    }

    private fun bindElements() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        toolbar = findViewById(R.id.toolbar)
        appBar = findViewById(R.id.appbar)
        ivProfileImage = findViewById(R.id.iv_profile_image)
        tvProfileName = findViewById(R.id.tv_profile_name)
        tvGeneralGoal = findViewById(R.id.tv_general_goal)
        rvFeedUser = findViewById(R.id.rv_feed_user)
        swipeRefresh = findViewById(R.id.swipe_refresh)
    }

    private fun init() {
        userId = intent.getIntExtra(EXTRA_PROFILE_ID, 0)
        title = intent.getStringExtra(EXTRA_PROFILE_NAME) as String

        setupToolbar(title)
        setupRecyclerView()

        tvProfileName!!.text = title
        swipeRefresh!!.setOnRefreshListener { profilePresenter!!.refresh(userId) }

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
        rvFeedUser!!.layoutManager = gridLayoutManager
        rvFeedUser!!.adapter = feedUserAdapter

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
        appBar!!.addOnOffsetChangedListener(this)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        collapsingToolbarLayout!!.title = title
        collapsingToolbarLayout!!.isTitleEnabled = false
        collapsingToolbarLayout!!.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
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
        if (isFirstLoad || swipeRefresh!!.isRefreshing) {
            isFirstLoad = false

            Picasso.get()
                .load(profile.imageUrl)
                .placeholder(R.drawable.profile_image_placeholder)
                .error(R.drawable.profile_image_placeholder)
                .fit()
                .centerCrop()
                .into(ivProfileImage)

            collapsingToolbarLayout!!.title = profile.name
            tvProfileName!!.text = profile.name

            tvGeneralGoal!!.text = profile.generalGoal

            feedUserAdapter.items = feedItems
            swipeRefresh!!.isRefreshing = false
            endlessRecyclerViewScrollListener.resetState()
            rvFeedUser!!.clearOnScrollListeners()
            rvFeedUser!!.addOnScrollListener(endlessRecyclerViewScrollListener)
        } else {
            feedUserAdapter.appendItems(feedItems)
        }
        if (feedItems.count() == 0)
            feedUserAdapter.notifyEndList()
    }

    override fun onLoadFail(error: Throwable) {
        swipeRefresh!!.isRefreshing = false
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
