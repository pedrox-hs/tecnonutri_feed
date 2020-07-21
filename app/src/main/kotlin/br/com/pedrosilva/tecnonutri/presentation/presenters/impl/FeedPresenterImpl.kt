package br.com.pedrosilva.tecnonutri.presentation.presenters.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter

class FeedPresenterImpl(
    executor: Executor,
    mainThread: MainThread,
    private val view: FeedPresenter.View,
    private val feedRepository: FeedRepository
) : AbstractPresenter(executor, mainThread),
    FeedPresenter,
    GetFeedInteractor.Callback,
    ChangeLikeInteractor.Callback {

    init {
        load()
    }

    override fun resume() {}

    override fun pause() {}

    override fun stop() {}

    override fun destroy() {}

    override fun onError(message: String) {}

    override fun onFeedRetrieved(
        feedItems: List<FeedItem>,
        page: Int,
        timestamp: Int
    ) {
        view.onLoadFeed(feedItems, page, timestamp)
    }

    override fun onFeedRetrieveFailed(error: Throwable) {
        error.printStackTrace()
        view.onLoadFail(error)
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

    override fun refresh() {
        load()
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

    override fun isLiked(feedHash: String): Boolean {
        return feedRepository.isLiked(feedHash)
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

    override fun onChange(feedHash: String, liked: Boolean) {
        view.onChangeLike(feedHash, liked)
    }
}