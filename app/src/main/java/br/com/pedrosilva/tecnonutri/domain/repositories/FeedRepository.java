package br.com.pedrosilva.tecnonutri.domain.repositories;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;

/**
 * Created by psilva on 3/16/17.
 */

public interface FeedRepository {

    void get(String feedHash, FeedItemCallback callback);

    void likeItem(String feedHash);

    void dislikeItem(String feedHash);

    boolean isLiked(String feedHash);

    void firstList(FeedCallback callback);

    void loadMore(int page, int timestamp, FeedCallback callback);

    interface FeedCallback {
        void onSuccess(List<FeedItem> feedItems, int page, int timestamp);
        void onError(Throwable t);
    }

    interface FeedItemCallback {
        void onSuccess(FeedItem feedItem);
        void onError(Throwable t);
    }
}
