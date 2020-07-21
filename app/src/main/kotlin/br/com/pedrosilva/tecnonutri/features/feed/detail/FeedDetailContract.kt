package br.com.pedrosilva.tecnonutri.features.feed.detail

import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.features.common.presenter.BasePresenter
import br.com.pedrosilva.tecnonutri.features.common.view.BaseView

interface FeedDetailContract {

    interface Presenter :
        BasePresenter<View> {

        fun reload()

        fun changeLike(feedHash: String, liked: Boolean)

        fun isLiked(feedHash: String): Boolean
    }

    interface View : BaseView {
        fun onLoadFeedItem(item: FeedItem)

        fun onLoadFail(error: Throwable)

        fun onChangeLike(feedHash: String, like: Boolean)
    }
}