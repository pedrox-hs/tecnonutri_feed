package br.com.pedrosilva.tecnonutri.features.feed.list

import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedInteractor
import javax.inject.Inject

class FeedListPresenter @Inject constructor(
    view: FeedListContract.View,
    private val getFeedInteractor: GetFeedInteractor,
    private val changeLikeInteractor: ChangeLikeInteractor,
) : AbstractPresenter<FeedListContract.View>(view),
    FeedListContract.Presenter,
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
        getFeedInteractor.execute(GetFeedInteractor.Params(), this)
    }

    override fun loadMore(page: Int, timestamp: Int) {
        getFeedInteractor.execute(GetFeedInteractor.Params(page, timestamp), this)
    }

    override fun changeLike(feedHash: String, liked: Boolean) {
        changeLikeInteractor.execute(ChangeLikeInteractor.Params(feedHash, liked), this)
    }

    override fun refresh() {
        load()
    }

    override fun onChange(feedHash: String, liked: Boolean) {
        view?.onChangeLike(feedHash, liked)
    }
}