package br.com.pedrosilva.tecnonutri.domain.interactors.impl

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedItemInteractor
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository.FeedItemCallback

class GetFeedItemInteractorImpl(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
    private val feedHash: String,
    private val callback: GetFeedItemInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetFeedItemInteractor {
    override fun run() {
        feedRepository.get(feedHash, object : FeedItemCallback {
            override fun onSuccess(feedItem: FeedItem) {
                mainThread.post { callback.onFeedItemRetrieved(feedItem) }
            }

            override fun onError(error: Throwable) {
                mainThread.post { callback.onFeedItemRetrieveFailed(error) }
            }
        })
    }
}