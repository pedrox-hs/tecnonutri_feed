package br.com.pedrosilva.tecnonutri.presentation.presenters;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter;
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView;

/**
 * Created by psilva on 3/18/17.
 */

public interface ProfilePresenter extends BasePresenter {
    interface View extends BaseView {
        void onLoadProfile(Profile profile, List<FeedItem> feedItems, int page, int timestamp);
        void onLoadFail(Throwable t);
    }

    void load(int userId);
    void loadMore(int userId, int page, int timestamp);
    void refresh(int userId);
}
