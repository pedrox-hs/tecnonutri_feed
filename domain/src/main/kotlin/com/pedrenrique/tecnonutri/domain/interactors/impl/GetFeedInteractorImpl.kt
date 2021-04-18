package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.GetFeedInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository.FeedCallback
import javax.inject.Inject

class GetFeedInteractorImpl @Inject constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
) : AbstractInteractor<GetFeedInteractor.Params, GetFeedInteractor.Callback>(
    threadExecutor,
    mainThread
), GetFeedInteractor {

    override fun run(params: GetFeedInteractor.Params, callback: GetFeedInteractor.Callback) =
        params.run {
            if (page == 0 || timestamp == 0) {
                firstList(callback)
            } else {
                loadMore(this, callback)
            }
        }

    private fun firstList(callback: GetFeedInteractor.Callback) {
        feedRepository.firstList(Callback(mainThread, callback))
    }

    private fun loadMore(params: GetFeedInteractor.Params, callback: GetFeedInteractor.Callback) =
        params.run {
            feedRepository.loadMore(page, timestamp, Callback(mainThread, callback))
        }

    private class Callback(
        private val mainThread: MainThread,
        private val callback: GetFeedInteractor.Callback,
    ) : FeedCallback {
        override fun onSuccess(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        ) {
            mainThread.post { callback.onFeedRetrieved(feedItems, page, timestamp) }
        }

        override fun onError(error: Throwable) {
            mainThread.post { callback.onFeedRetrieveFailed(error) }
        }
    }
}