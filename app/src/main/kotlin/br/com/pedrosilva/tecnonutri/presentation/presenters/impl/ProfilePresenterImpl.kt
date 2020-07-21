package br.com.pedrosilva.tecnonutri.presentation.presenters.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor
import com.pedrenrique.tecnonutri.domain.interactors.impl.GetProfileInteractorImpl
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import br.com.pedrosilva.tecnonutri.presentation.presenters.ProfilePresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter

class ProfilePresenterImpl(
    executor: Executor,
    mainThread: MainThread,
    private val view: ProfilePresenter.View,
    private val profileRepository: ProfileRepository,
    userId: Int
) : AbstractPresenter(executor, mainThread),
    ProfilePresenter,
    GetProfileInteractor.Callback {

    init {
        load(userId)
    }

    override fun load(userId: Int) {
        val interactor: Interactor = GetProfileInteractorImpl(
            executor,
            mainThread,
            profileRepository,
            userId,
            this
        )
        interactor.execute()
    }

    override fun loadMore(userId: Int, page: Int, timestamp: Int) {
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

    override fun refresh(userId: Int) {
        load(userId)
    }

    override fun onProfileRetrieved(
        profile: Profile,
        feedItems: List<FeedItem>,
        page: Int,
        timestamp: Int
    ) {
        view.onLoadProfile(profile, feedItems, page, timestamp)
    }

    override fun onProfileRetrieveFailed(error: Throwable) {
        view.onLoadFail(error)
    }

    override fun resume() {}

    override fun pause() {}

    override fun stop() {}

    override fun destroy() {}

    override fun onError(message: String) {}
}