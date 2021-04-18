package br.com.pedrosilva.tecnonutri.features.profile

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import br.com.pedrosilva.tecnonutri.features.common.presenter.BasePresenter
import br.com.pedrosilva.tecnonutri.features.common.view.BaseView

interface ProfileContract {

    interface Presenter :
        BasePresenter<View> {

        fun loadMore(page: Int, timestamp: Int)

        fun refresh()
    }

    interface View : BaseView {

        val profileUserId: Int

        fun onLoadProfile(
            profile: Profile,
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onLoadFail(error: Throwable)
    }
}