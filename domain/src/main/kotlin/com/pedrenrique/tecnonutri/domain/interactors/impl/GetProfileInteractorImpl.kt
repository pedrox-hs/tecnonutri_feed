package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetProfileInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository.ProfileCallback

class GetProfileInteractorImpl @JvmOverloads constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val profileRepository: ProfileRepository,
    private val userId: Int,
    private val callback: GetProfileInteractor.Callback,
    private var page: Int? = null,
    private var timestamp: Int? = null
) : AbstractInteractor(threadExecutor, mainThread), GetProfileInteractor {

    override fun run() {
        if (page == null) {
            firstList(userId)
        } else {
            loadMore(userId, page!!, timestamp!!)
        }
    }

    private fun firstList(userId: Int) {
        profileRepository.get(userId, Callback())
    }

    private fun loadMore(userId: Int, page: Int, timestamp: Int) {
        profileRepository.loadMorePosts(userId, page, timestamp, Callback())
    }

    private inner class Callback : ProfileCallback {
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