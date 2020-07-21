package br.com.pedrosilva.tecnonutri.features.feed.list

import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.features.common.presenter.BasePresenter
import br.com.pedrosilva.tecnonutri.features.common.view.BaseView

interface FeedListContract {

    interface FeedPresenter :
        BasePresenter<View> {

        fun load()

        fun loadMore(page: Int, timestamp: Int)

        fun refresh()

        fun changeLike(feedHash: String, liked: Boolean)

        fun isLiked(feedHash: String): Boolean
    }

    interface View : BaseView {

        fun onLoadFeed(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onLoadFail(error: Throwable)

        fun onChangeLike(feedHash: String, like: Boolean)
    }
}