package br.com.pedrosilva.tecnonutri.features.feed.list

import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository

class FeedListPresenter(
    view: FeedListContract.View,
    executor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository
) : AbstractPresenter<FeedListContract.View>(view, executor, mainThread),
    FeedListContract.FeedPresenter,
    GetFeedInteractor.Callback,
    ChangeLikeInteractor.Callback {

    override fun create() {
        load()
    }

    override fun onFeedRetrieved(feedItems: List<FeedItem>, page: Int, timestamp: Int) {
        view?.onLoadFeed(feedItems, page, timestamp)
    }

    override fun onFeedRetrieveFailed(error: Throwable) {
        error.printStackTrace()
        view?.onLoadFail(error)
    }

    override fun load() {
        val interactor: Interactor = GetFeedInteractorImpl(
            executor,
            mainThread,
            feedRepository,
            this
        )
        interactor.execute()
    }

    override fun loadMore(page: Int, timestamp: Int) {
        val interactor: Interactor = GetFeedInteractorImpl(
            executor,
            mainThread,
            feedRepository,
            this,
            page,
            timestamp
        )
        interactor.execute()
    }

    override fun changeLike(feedHash: String, liked: Boolean) {
        val interactor: Interactor = ChangeLikeInteractorImpl(
            executor,
            mainThread,
            feedRepository,
            feedHash,
            liked,
            this
        )
        interactor.execute()
    }

    override fun refresh() {
        load()
    }

    override fun isLiked(feedHash: String): Boolean =
        feedRepository.isLiked(feedHash)

    override fun onChange(feedHash: String, liked: Boolean) {
        view?.onChangeLike(feedHash, liked)
    }
}