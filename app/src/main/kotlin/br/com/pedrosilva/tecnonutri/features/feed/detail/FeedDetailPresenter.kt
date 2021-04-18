package br.com.pedrosilva.tecnonutri.features.feed.detail

import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor
import javax.inject.Inject

class FeedDetailPresenter @Inject constructor(
    view: FeedDetailContract.View,
    private val getFeedItemInteractor: GetFeedItemInteractor,
    private val changeLikeInteractor: ChangeLikeInteractor,
) : AbstractPresenter<FeedDetailContract.View>(view),
    FeedDetailContract.Presenter,
    GetFeedItemInteractor.Callback,
    ChangeLikeInteractor.Callback {

    private val feedHash: String by lazy {
        super.view?.currentFeedHash.orEmpty()
    }

    private fun load() {
        //view?.showProgress();
        getFeedItemInteractor.execute(GetFeedItemInteractor.Params(feedHash), this)
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

    override fun changeLike(liked: Boolean) {
        changeLikeInteractor.execute(ChangeLikeInteractor.Params(feedHash, liked), this)
    }

    override fun onChange(feedHash: String, liked: Boolean) {
        view?.onChangeLike(feedHash, liked)
    }
}