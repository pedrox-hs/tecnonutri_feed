package br.com.pedrosilva.tecnonutri.features.profile

import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import javax.inject.Inject

class ProfilePresenter @Inject constructor(
    view: ProfileContract.View,
    private val getProfileInteractor: GetProfileInteractor,
) : AbstractPresenter<ProfileContract.View>(view),
    ProfileContract.Presenter,
    GetProfileInteractor.Callback {

    private val userId: Int by lazy {
        super.view?.profileUserId ?: 0
    }

    override fun create() {
        load()
    }

    private fun load() {
        getProfileInteractor.execute(GetProfileInteractor.Params(userId), this)
    }

    override fun loadMore(page: Int, timestamp: Int) {
        getProfileInteractor.execute(GetProfileInteractor.Params(userId, page, timestamp), this)
    }

    override fun refresh() {
        load()
    }

    override fun onProfileRetrieved(
        profile: Profile,
        feedItems: List<FeedItem>,
        page: Int,
        timestamp: Int
    ) {
        view?.onLoadProfile(profile, feedItems, page, timestamp)
    }

    override fun onProfileRetrieveFailed(error: Throwable) {
        view?.onLoadFail(error)
    }
}