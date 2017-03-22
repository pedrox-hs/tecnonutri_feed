package br.com.pedrosilva.tecnonutri.domain.interactors.impl;

import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.ChangeLikeInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;

/**
 * Created by psilva on 3/21/17.
 */

public class ChangeLikeInteractorImpl extends AbstractInteractor implements ChangeLikeInteractor {
    private final String feedHash;
    private final boolean liked;
    private final Callback callback;
    private final FeedRepository feedRepository;

    public ChangeLikeInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                    FeedRepository repository, String feedHash, boolean liked,
                                    Callback callback) {
        super(threadExecutor, mainThread);

        this.callback = callback;
        this.feedRepository = repository;
        this.feedHash = feedHash;
        this.liked = liked;
    }

    @Override
    public void run() {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                if (liked) {
                    feedRepository.likeItem(feedHash);
                } else {
                    feedRepository.dislikeItem(feedHash);
                }
                callback.onChange(feedHash, liked);
            }
        });
    }
}
