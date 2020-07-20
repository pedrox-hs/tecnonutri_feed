package br.com.pedrosilva.tecnonutri.presentation.presenters

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView

interface ProfilePresenter : BasePresenter {

    interface View : BaseView {
        fun onLoadProfile(
            profile: Profile,
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onLoadFail(error: Throwable)
    }

    fun load(userId: Int)

    fun loadMore(userId: Int, page: Int, timestamp: Int)

    fun refresh(userId: Int)
}