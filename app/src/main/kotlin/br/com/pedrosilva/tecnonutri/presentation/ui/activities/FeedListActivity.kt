package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FeedListAdapter
import br.com.pedrosilva.tecnonutri.presentation.ui.HasPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.error.ErrorData
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.EndlessRecyclerViewScrollListener
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedListContract
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.FeedListPresenter
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl
import com.pedrenrique.tecnonutri.data.repositories.FeedRepositoryImpl
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.impl.ThreadExecutor
import kotlinx.android.synthetic.main.activity_main.rv_feed
import kotlinx.android.synthetic.main.activity_main.swipe_refresh

class FeedListActivity : BaseActivity(), FeedListContract.View,
    HasPresenter {

    override val presenter: FeedListContract.FeedPresenter by lazy {
        FeedListPresenter(
            this,
            ThreadExecutor.instance,
            MainThreadImpl.instance,
            FeedRepositoryImpl()
        )
    }
    private val feedAdapter by lazy { FeedListAdapter() }
    private val RecyclerView.linearLayoutManager: LinearLayoutManager
        get() = layoutManager as LinearLayoutManager
    private var timestamp = 0
    private var nextPage = 0
    private var isFirstLoad = true
    private var isEndList = false
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupRecyclerView()
        swipe_refresh.setOnRefreshListener { presenter.refresh() }
    }

    private fun setupRecyclerView() {
        feedAdapter.setFeedItemClickListener(::onFeedItemClick)
        feedAdapter.setProfileClickListener(::onProfileClick)
        feedAdapter.setChangeLikeListener { feedHash, liked ->
            presenter.changeLike(feedHash, liked)
        }
        feedAdapter.setRetryClickListener {
            if (isFirstLoad) {
                presenter.load()
            } else {
                presenter.loadMore(nextPage, timestamp)
            }
        }
        rv_feed.apply {
            setHasFixedSize(true)
            adapter = feedAdapter
        }
        endlessRecyclerViewScrollListener =
            EndlessRecyclerViewScrollListener(
                rv_feed.linearLayoutManager
            ) { _, _ ->
                presenter.loadMore(nextPage, timestamp)
            }
    }

    override fun onResume() {
        super.onResume()
        feedAdapter.run {
            items.map { item -> item.copy(isLiked = presenter.isLiked(item.id)) }
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
        if (isFirstLoad || swipe_refresh.isRefreshing) {
            isFirstLoad = false
            feedAdapter.items = feedItems
            swipe_refresh.isRefreshing = false
            isEndList = false
            endlessRecyclerViewScrollListener!!.resetState()
            rv_feed.clearOnScrollListeners()
            rv_feed.addOnScrollListener(endlessRecyclerViewScrollListener!!)
        } else {
            feedAdapter.appendItems(feedItems)
        }
        isEndList = feedItems.isEmpty()
    }

    override fun onLoadFail(error: Throwable) {
        swipe_refresh.isRefreshing = false
        val msgError = getString(R.string.fail_to_load_try_again)
        feedAdapter.notifyError(msgError)
        presenter.onError(ErrorData(msgError))
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