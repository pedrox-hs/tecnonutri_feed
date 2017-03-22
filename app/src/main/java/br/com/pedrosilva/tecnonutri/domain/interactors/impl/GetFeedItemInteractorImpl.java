package br.com.pedrosilva.tecnonutri.domain.interactors.impl;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedItemInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;

/**
 * Created by psilva on 3/20/17.
 */

public class GetFeedItemInteractorImpl extends AbstractInteractor implements GetFeedItemInteractor {
    private final Callback callback;
    private final FeedRepository feedRepository;
    private final String feedHash;

    public GetFeedItemInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     FeedRepository repository, String feedHash, Callback callback) {
        super(threadExecutor, mainThread);

        this.feedHash = feedHash;
        this.feedRepository = repository;
        this.callback = callback;
    }

    @Override
    public void run() {
        feedRepository.get(feedHash, new FeedRepository.FeedItemCallback() {
            @Override
            public void onSuccess(final FeedItem feedItem) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedItemRetrieved(feedItem);
                    }
                });
            }

            @Override
            public void onError(final Throwable t) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedItemRetrieveFailed(t);
                    }
                });
            }
        });
    }
}
