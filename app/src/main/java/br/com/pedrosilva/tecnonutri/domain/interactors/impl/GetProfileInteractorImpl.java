package br.com.pedrosilva.tecnonutri.domain.interactors.impl;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.domain.executor.Executor;
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread;
import br.com.pedrosilva.tecnonutri.domain.interactors.GetProfileInteractor;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor;
import br.com.pedrosilva.tecnonutri.domain.repositories.ProfileRepository;

/**
 * Created by psilva on 3/18/17.
 */

public class GetProfileInteractorImpl extends AbstractInteractor implements GetProfileInteractor {

    private GetProfileInteractor.Callback callback;
    private ProfileRepository profileRepository;
    private int userId;

    private Integer page;
    private Integer timestamp;

    public GetProfileInteractorImpl(Executor threadExecutor,
                                    MainThread mainThread, ProfileRepository repository,
                                    int userId,
                                    GetProfileInteractor.Callback callback) {
        super(threadExecutor, mainThread);

        this.callback = callback;
        this.profileRepository = repository;
        this.userId = userId;
    }

    public GetProfileInteractorImpl(Executor threadExecutor,
                                    MainThread mainThread, ProfileRepository repository,
                                    int userId,
                                    int page, int timestamp,
                                    GetProfileInteractor.Callback callback) {
        this(threadExecutor, mainThread, repository, userId, callback);

        this.page = page;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        if (page == null) {
            firstList(userId);
        } else {
            loadMore(userId, page, timestamp);
        }
    }

    private void firstList(int userId) {
        profileRepository.get(userId, new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(final Profile profile, final List<FeedItem> feedItems, final int page, final int timestamp) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProfileRetrieved(profile, feedItems, page, timestamp);
                    }
                });
            }

            @Override
            public void onError(final Throwable t) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProfileRetrieveFailed(t);
                    }
                });
            }
        });
    }

    private void loadMore(int userId, final int page, final int timestamp) {
        profileRepository.loadMorePosts(userId, page, timestamp, new ProfileRepository.ProfileCallback() {
            @Override
            public void onSuccess(final Profile profile, final List<FeedItem> feedItems, final int page, final int timestamp) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProfileRetrieved(profile, feedItems, page, timestamp);
                    }
                });
            }

            @Override
            public void onError(final Throwable t) {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProfileRetrieveFailed(t);
                    }
                });
            }
        });
    }
}
