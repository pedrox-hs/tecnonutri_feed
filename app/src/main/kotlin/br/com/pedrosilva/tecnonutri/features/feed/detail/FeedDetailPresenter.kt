package br.com.pedrosilva.tecnonutri.features.feed.detail

import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetFeedItemInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository

class FeedDetailPresenter(
    view: FeedDetailContract.View,
    executor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
    private val feedHash: String
) : AbstractPresenter<FeedDetailContract.View>(view, executor, mainThread),
    FeedDetailContract.Presenter,
    GetFeedItemInteractor.Callback,
    ChangeLikeInteractor.Callback {

    private fun load() {
        //view?.showProgress();
        val interactor: Interactor = GetFeedItemInteractorImpl(
            executor,
            mainThread,
            feedRepository,
            feedHash,
            this
        )
        interactor.execute()
    }

    override fun create() {
        load()
    }

    override fun onFeedItemRetrieved(feedItem: FeedItem) {
        view?.hideProgress()
        view?.onLoadFeedItem(feedItem)
    }

    override fun onFeedItemRetrieveFailed(error: Throwable) {
        view?.onLoadFail(error)
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

    override fun isLiked(feedHash: String): Boolean =
        feedRepository.isLiked(feedHash)

    override fun onChange(feedHash: String, liked: Boolean) {
        view?.onChangeLike(feedHash, liked)
    }
}