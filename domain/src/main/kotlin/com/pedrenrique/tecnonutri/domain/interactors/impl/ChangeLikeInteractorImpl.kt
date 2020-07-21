package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository

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