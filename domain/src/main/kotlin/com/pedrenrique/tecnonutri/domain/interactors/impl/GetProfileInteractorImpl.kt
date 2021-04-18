package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor.Callback
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor.Params
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository.ProfileCallback
import javax.inject.Inject

class GetProfileInteractorImpl @Inject constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val profileRepository: ProfileRepository
) : AbstractInteractor<Params, Callback>(threadExecutor, mainThread), GetProfileInteractor {

    override fun run(params: Params, callback: GetProfileInteractor.Callback) {
        if (params.page == 0) {
            firstList(params, callback)
        } else {
            loadMore(params, callback)
        }
    }

    private fun firstList(params: Params, callback: GetProfileInteractor.Callback) {
        profileRepository.get(params.userId, Callback(mainThread, callback))
    }

    private fun loadMore(params: Params, callback: GetProfileInteractor.Callback) = params.run {
        profileRepository.loadMorePosts(userId, page, timestamp, Callback(mainThread, callback))
    }

    private class Callback(
        private val mainThread: MainThread,
        private val callback: GetProfileInteractor.Callback
    ) : ProfileCallback {
        override fun onSuccess(
            profile: Profile,
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        ) {
            mainThread.post { callback.onProfileRetrieved(profile, feedItems, page, timestamp) }
        }

        override fun onError(t: Throwable) {
            mainThread.post { callback.onProfileRetrieveFailed(t) }
        }
    }
}