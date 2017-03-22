package br.com.pedrosilva.tecnonutri.presentation.presenters.impl;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.ChangeLikeInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor;
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl;
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.GetFeedInteractorImpl;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedPresenter;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter;

/**
 * Created by dmilicic on 12/13/15.
 */
public class FeedPresenterImpl extends AbstractPresenter implements FeedPresenter,
        GetFeedInteractor.Callback, ChangeLikeInteractor.Callback {

    private FeedPresenter.View view;
    private FeedRepository feedRepository;

    public FeedPresenterImpl(Executor executor,
                             MainThread mainThread,
                             View view, FeedRepository feedRepository) {
        super(executor, mainThread);
        this.view = view;
        this.feedRepository = feedRepository;

        load();
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onFeedRetrieved(List<FeedItem> feedItems, int page, int timestamp) {
        view.onLoadFeed(feedItems, page, timestamp);
    }

    @Override
    public void onFeedRetrieveFailed(Throwable t) {
        view.onLoadFail(t);
    }

    @Override
    public void load() {
        Interactor interactor = new GetFeedInteractorImpl(
                executor,
                mainThread,
                feedRepository,
                this
        );
        interactor.execute();
    }

    @Override
    public void refresh() {
        load();
    }

    @Override
    public void changeLike(String feedHash, boolean liked) {
        Interactor interactor = new ChangeLikeInteractorImpl(
                executor,
                mainThread,
                feedRepository,
                feedHash,
                liked,
                this
        );
        interactor.execute();
    }

    @Override
    public boolean isLiked(String feedHash) {
        return feedRepository.isLiked(feedHash);
    }

    @Override
    public void loadMore(int page, int timestamp) {
        Interactor interactor = new GetFeedInteractorImpl(
                executor,
                mainThread,
                feedRepository,
                page, timestamp,
                this
        );
        interactor.execute();
    }

    @Override
    public void onChange(String feedHash, boolean liked) {
        view.onChangeLike(feedHash, liked);
    }
}
