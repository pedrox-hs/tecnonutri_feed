package br.com.pedrosilva.tecnonutri.domain.interactors.impl

import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread
import br.com.pedrosilva.tecnonutri.domain.interactors.ChangeLikeInteractor
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository

class ChangeLikeInteractorImpl(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
    private val feedHash: String,
    private val liked: Boolean,
    private val callback: ChangeLikeInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), ChangeLikeInteractor {
    override fun run() {
        mainThread.post {
            if (liked) {
                feedRepository.likeItem(feedHash)
            } else {
                feedRepository.dislikeItem(feedHash)
            }
            callback.onChange(feedHash, liked)
        }
    }
}