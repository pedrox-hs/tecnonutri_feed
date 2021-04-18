package com.pedrenrique.tecnonutri.domain.interactors.impl

import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor.Callback
import com.pedrenrique.tecnonutri.domain.interactors.ChangeLikeInteractor.Params
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import javax.inject.Inject

class ChangeLikeInteractorImpl @Inject constructor(
    threadExecutor: Executor,
    mainThread: MainThread,
    private val feedRepository: FeedRepository,
) : AbstractInteractor<Params, Callback>(threadExecutor, mainThread), ChangeLikeInteractor {
    override fun run(params: Params, callback: Callback) = params.run {
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