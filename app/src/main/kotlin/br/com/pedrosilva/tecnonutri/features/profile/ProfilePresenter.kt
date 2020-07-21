package br.com.pedrosilva.tecnonutri.features.profile

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetProfileInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import br.com.pedrosilva.tecnonutri.features.common.presenter.AbstractPresenter

class ProfilePresenter(
    view: ProfileContract.View,
    executor: Executor,
    mainThread: MainThread,
    private val profileRepository: ProfileRepository,
    private val userId: Int
) : AbstractPresenter<ProfileContract.View>(view, executor, mainThread),
    ProfileContract.Presenter,
    GetProfileInteractor.Callback {

    override fun create() {
        load()
    }

    private fun load() {
        val interactor: Interactor = GetProfileInteractorImpl(
            executor,
            mainThread,
            profileRepository,
            userId,
            this
        )
        interactor.execute()
    }

    override fun loadMore(page: Int, timestamp: Int) {
        val interactor: Interactor = GetProfileInteractorImpl(
            executor,
            mainThread,
            profileRepository,
            userId,
            this,
            page,
            timestamp
        )
        interactor.execute()
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