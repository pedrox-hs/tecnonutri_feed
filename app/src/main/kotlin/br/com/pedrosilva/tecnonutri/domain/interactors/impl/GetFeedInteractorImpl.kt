package br.com.pedrosilva.tecnonutri.domain.interactors.impl

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedInteractor
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository.FeedCallback

class GetFeedInteractorImpl @JvmOverloads constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
    private val callback: GetFeedInteractor.Callback,
    private var page: Int? = null,
    private var timestamp: Int? = null
) : AbstractInteractor(threadExecutor, mainThread), GetFeedInteractor {

    override fun run() {
        if (page == null || timestamp == null) {
            firstList()
        } else {
            loadMore(page!!, timestamp!!)
        }
    }

    private fun firstList() {
        feedRepository.firstList(Callback())
    }

    private fun loadMore(page: Int, timestamp: Int) {
        feedRepository.loadMore(page, timestamp, Callback())
    }

    private inner class Callback : FeedCallback {
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