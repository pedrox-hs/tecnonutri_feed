package br.com.pedrosilva.tecnonutri.presentation.presenters.impl

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread
import br.com.pedrosilva.tecnonutri.domain.interactors.ChangeLikeInteractor
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedInteractor
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.GetFeedInteractorImpl
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository
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