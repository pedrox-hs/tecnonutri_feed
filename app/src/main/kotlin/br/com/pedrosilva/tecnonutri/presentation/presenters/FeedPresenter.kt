package br.com.pedrosilva.tecnonutri.presentation.presenters

import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView

interface FeedPresenter : BasePresenter {

    interface View : BaseView {
        fun onLoadFeed(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onLoadFail(error: Throwable)

        fun onChangeLike(feedHash: String, like: Boolean)
    }

    fun load()

    fun loadMore(page: Int, timestamp: Int)

    fun refresh()

    fun changeLike(feedHash: String, liked: Boolean)

    fun isLiked(feedHash: String): Boolean
}