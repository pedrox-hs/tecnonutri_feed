package br.com.pedrosilva.tecnonutri.presentation.presenters;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter;
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView;


public interface FeedPresenter extends BasePresenter {

    interface View extends BaseView {
        void onLoadFeed(List<FeedItem> feedItems, int page, int timestamp);
        void onLoadFail(Throwable t);
        void onChangeLike(String feedHash, boolean like);
    }

    void load();
    void loadMore(int page, int timestamp);
    void refresh();
    void changeLike(String feedHash, boolean liked);
    boolean isLiked(String feedHash);
}
