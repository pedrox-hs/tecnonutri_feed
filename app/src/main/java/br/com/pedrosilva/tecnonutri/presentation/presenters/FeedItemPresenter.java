package br.com.pedrosilva.tecnonutri.presentation.presenters;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter;
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView;

/**
 * Created by psilva on 3/20/17.
 */

public interface FeedItemPresenter extends BasePresenter {
    interface View extends BaseView {
        void onLoadFeedItem(FeedItem feedItem);
        void onLoadFail(Throwable t);
        void onChangeLike(String feedHash, boolean like);
    }

    void reload();
    void changeLike(String feedHash, boolean liked);
    boolean isLiked(String feedHash);
}
