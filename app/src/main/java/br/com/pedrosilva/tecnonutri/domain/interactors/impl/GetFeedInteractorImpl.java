package br.com.pedrosilva.tecnonutri.domain.interactors.impl;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;

/**
 * Created by psilva on 3/16/17.
 */
public class GetFeedInteractorImpl extends AbstractInteractor implements GetFeedInteractor {

    private Callback callback;
    private FeedRepository feedRepository;

    private Integer page;
    private Integer timestamp;

    public GetFeedInteractorImpl(Executor threadExecutor,
                                 MainThread mainThread, FeedRepository repository,
                                 Callback callback) {
        super(threadExecutor, mainThread);

        this.callback = callback;
        this.feedRepository = repository;
    }

    public GetFeedInteractorImpl(Executor threadExecutor,
                                 MainThread mainThread, FeedRepository repository,
                                 int page, int timestamp,
                                 Callback callback) {
        this(threadExecutor, mainThread, repository, callback);

        this.page = page;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        if (page == null) {
            firstList();
        } else {
            loadMore(page, timestamp);
        }
    }

    private void firstList() {
        feedRepository.firstList(new FeedRepository.FeedCallback() {
            @Override
            public void onSuccess(final List<FeedItem> feedItems, final int page, final int timestamp) {

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedRetrieved(feedItems, page, timestamp);
                    }
                });
            }

            @Override
            public void onError(final Throwable t) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedRetrieveFailed(t);
                    }
                });
            }
        });
    }

    private void loadMore(int page, int timestamp) {
        feedRepository.loadMore(page, timestamp, new FeedRepository.FeedCallback() {
            @Override
            public void onSuccess(final List<FeedItem> feedItems, final int page, final int timestamp) {

                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedRetrieved(feedItems, page, timestamp);
                    }
                });
            }

            @Override
            public void onError(final Throwable t) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFeedRetrieveFailed(t);
                    }
                });
            }
        });
    }
}
