package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.data.repositories.FeedRepositoryImpl
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile
import br.com.pedrosilva.tecnonutri.domain.executor.impl.ThreadExecutor
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.FeedPresenterImpl
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FeedAdapter
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.EndlessRecyclerViewScrollListener
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl

class MainActivity : BaseActivity(), FeedPresenter.View {

    private var feedPresenter: FeedPresenter? = null
    private var rvFeed: RecyclerView? = null
    private val feedAdapter by lazy { FeedAdapter() }
    private var swipeRefresh: SwipeRefreshLayout? = null
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }
    private var timestamp = 0
    private var nextPage = 0
    private var isFirstLoad = true
    private var isEndList = false
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindElements()
        init()
    }

    private fun init() {
        setupRecyclerView()
        swipeRefresh!!.setOnRefreshListener { feedPresenter!!.refresh() }
        feedPresenter = FeedPresenterImpl(
            ThreadExecutor.instance,
            MainThreadImpl.instance,
            this,
            FeedRepositoryImpl()
        )
    }

    private fun setupRecyclerView() {
        feedAdapter.setFeedItemClickListener(::onFeedItemClick)
        feedAdapter.setProfileClickListener(::onProfileClick)
        feedAdapter.setChangeLikeListener { feedHash, liked ->
            feedPresenter!!.changeLike(feedHash, liked)
        }
        feedAdapter.setRetryClickListener {
            if (isFirstLoad) {
                feedPresenter?.load()
            } else {
                feedPresenter?.loadMore(nextPage, timestamp)
            }
        }
        rvFeed?.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = feedAdapter
        }
        endlessRecyclerViewScrollListener =
            EndlessRecyclerViewScrollListener(linearLayoutManager) { _, _ ->
                feedPresenter?.loadMore(nextPage, timestamp)
            }
    }

    private fun bindElements() {
        rvFeed = findViewById<View>(R.id.rv_feed) as RecyclerView
        swipeRefresh = findViewById<View>(R.id.swipe_refresh) as SwipeRefreshLayout
    }

    override fun onResume() {
        super.onResume()
        feedPresenter?.resume()
        feedAdapter.run {
            items.map { item ->
                item.copy(isLiked = feedPresenter!!.isLiked(item.id))
            }
            notifyDataSetChanged()
        }
    }

    override fun onLoadFeed(
        feedItems: List<FeedItem>,
        page: Int,
        timestamp: Int
    ) {
        this.timestamp = timestamp
        nextPage = page + 1
        if (isFirstLoad || swipeRefresh!!.isRefreshing) {
            isFirstLoad = false
            feedAdapter.items = feedItems
            swipeRefresh!!.isRefreshing = false
            isEndList = false
            endlessRecyclerViewScrollListener!!.resetState()
            rvFeed!!.clearOnScrollListeners()
            rvFeed!!.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        } else {
            feedAdapter.appendItems(feedItems)
        }
        isEndList = feedItems.isEmpty()
    }

    override fun onLoadFail(error: Throwable) {
        swipeRefresh!!.isRefreshing = false
        val msgError = getString(R.string.fail_to_load_try_again)
        feedAdapter.notifyError(msgError)
        showError(msgError)
    }

    override fun onChangeLike(feedHash: String, like: Boolean) {}

    private fun onProfileClick(profile: Profile) {
        Navigator.navigateToProfile(
            this,
            profile.id,
            profile.name
        )
    }

    private fun onFeedItemClick(feedItem: FeedItem) {
        Navigator.navigateToFeedItemDetails(
            this,
            feedItem.id,
            feedItem.date
        )
    }
}