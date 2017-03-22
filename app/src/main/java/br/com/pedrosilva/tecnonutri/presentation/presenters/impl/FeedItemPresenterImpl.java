package br.com.pedrosilva.tecnonutri.presentation.presenters.impl;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.ChangeLikeInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetFeedItemInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor;
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.ChangeLikeInteractorImpl;
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.GetFeedItemInteractorImpl;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedItemPresenter;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter;

/**
 * Created by psilva on 3/20/17.
 */

public class FeedItemPresenterImpl extends AbstractPresenter implements FeedItemPresenter, GetFeedItemInteractor.Callback, ChangeLikeInteractor.Callback {
    private final View view;
    private final FeedRepository feedRepository;
    private final String feedHash;

    public FeedItemPresenterImpl(Executor executor,
                                 MainThread mainThread,
                                 View view, FeedRepository feedRepository, String feedHash) {
        super(executor, mainThread);

        this.view = view;
        this.feedRepository = feedRepository;
        this.feedHash = feedHash;

        load();
    }

    public void load() {
        //view.showProgress();
        Interactor interactor = new GetFeedItemInteractorImpl(
                executor,
                mainThread,
                feedRepository,
                feedHash,
                this
        );
        interactor.execute();
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
    public void onFeedItemRetrieved(FeedItem feedItem) {
        view.hideProgress();
        view.onLoadFeedItem(feedItem);
    }

    @Override
    public void onFeedItemRetrieveFailed(Throwable t) {
        view.onLoadFail(t);
    }

    @Override
    public void reload() {
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
    public void onChange(String feedHash, boolean liked) {
        view.onChangeLike(feedHash, liked);
    }
}
