package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor.Callback
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedItemInteractor.Params
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository.FeedItemCallback
import javax.inject.Inject

class GetFeedItemInteractorImpl @Inject constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository
) : AbstractInteractor<Params, Callback>(threadExecutor, mainThread), GetFeedItemInteractor {
    override fun run(params: Params, callback: Callback) {
        feedRepository.get(params.feedHash, object : FeedItemCallback {
            override fun onSuccess(feedItem: FeedItem) {
                mainThread.post { callback.onFeedItemRetrieved(feedItem) }
            }

            override fun onError(error: Throwable) {
                mainThread.post { callback.onFeedItemRetrieveFailed(error) }
            }
        })
    }
}