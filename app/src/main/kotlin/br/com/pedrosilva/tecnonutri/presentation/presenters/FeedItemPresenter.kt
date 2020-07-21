package br.com.pedrosilva.tecnonutri.presentation.presenters

import com.pedrenrique.tecnonutri.domain.FeedItem
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView

interface FeedItemPresenter : BasePresenter {

    interface View : BaseView {

        fun onLoadFeedItem(item: FeedItem)

        fun onLoadFail(error: Throwable)

        fun onChangeLike(feedHash: String, like: Boolean)
    }

    fun reload()

    fun changeLike(feedHash: String, liked: Boolean)

    fun isLiked(feedHash: String): Boolean
}