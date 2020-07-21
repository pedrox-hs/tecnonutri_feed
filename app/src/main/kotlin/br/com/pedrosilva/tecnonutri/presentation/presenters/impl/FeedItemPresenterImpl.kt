package br.com.pedrosilva.tecnonutri.presentation.presenters.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedItemInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedItemPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter

class FeedItemPresenterImpl(
    executor: Executor,
    mainThread: MainThread,
    private val view: FeedItemPresenter.View,
    private val feedRepository: FeedRepository,
    private val feedHash: String
) : AbstractPresenter(executor, mainThread),
    FeedItemPresenter,
    GetFeedItemInteractor.Callback,
    ChangeLikeInteractor.Callback {

    init {
        load()
    }

    fun load() {
        //view.showProgress();
        val interactor: Interactor = GetFeedItemInteractorImpl(
            executor,
            mainThread,
            feedRepository,
            feedHash,
            this
        )
        interactor.execute()
    }

    override fun resume() {}

    override fun pause() {}

    override fun stop() {}

    override fun destroy() {}

    override fun onError(message: String) {}

    override fun onFeedItemRetrieved(feedItem: FeedItem) {
        view.hideProgress()
        view.onLoadFeedItem(feedItem)
    }

    override fun onFeedItemRetrieveFailed(error: Throwable) {
        view.onLoadFail(error)
    }

    override fun reload() {
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

    override fun onChange(feedHash: String, liked: Boolean) {
        view.onChangeLike(feedHash, liked)
    }
}