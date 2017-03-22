package br.com.pedrosilva.tecnonutri.presentation.presenters.impl;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetProfileInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor;
import br.com.pedrosilva.tecnonutri.domain.interactors.impl.GetProfileInteractorImpl;
import br.com.pedrosilva.tecnonutri.domain.repositories.ProfileRepository;
import br.com.pedrosilva.tecnonutri.presentation.presenters.ProfilePresenter;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.AbstractPresenter;

/**
 * Created by psilva on 3/19/17.
 */

public class ProfilePresenterImpl extends AbstractPresenter implements ProfilePresenter, GetProfileInteractor.Callback {

    private View view;
    private ProfileRepository profileRepository;

    public ProfilePresenterImpl(Executor executor, MainThread mainThread, View view, ProfileRepository profileRepository, int userId) {
        super(executor, mainThread);

        this.view = view;
        this.profileRepository = profileRepository;

        load(userId);
    }

    @Override
    public void load(int userId) {
        Interactor interactor = new GetProfileInteractorImpl(
                executor,
                mainThread,
                profileRepository,
                userId,
                this
        );
        interactor.execute();
    }

    @Override
    public void loadMore(int userId, int page, int timestamp) {
        Interactor interactor = new GetProfileInteractorImpl(
                executor,
                mainThread,
                profileRepository,
                userId,
                page, timestamp,
                this
        );
        interactor.execute();
    }

    @Override
    public void refresh(int userId) {
        load(userId);
    }

    @Override
    public void onProfileRetrieved(Profile profile, List<FeedItem> feedItems, int page, int timestamp) {
        view.onLoadProfile(profile, feedItems, page, timestamp);
    }

    @Override
    public void onProfileRetrieveFailed(Throwable t) {
        view.onLoadFail(t);
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
}
