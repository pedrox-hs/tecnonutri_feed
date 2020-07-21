package br.com.pedrosilva.tecnonutri.presentation.presenters

import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView

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