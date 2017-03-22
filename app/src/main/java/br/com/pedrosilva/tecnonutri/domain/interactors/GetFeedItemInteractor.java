package br.com.pedrosilva.tecnonutri.domain.interactors;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;

/**
 * Created by psilva on 3/20/17.
 */

public interface GetFeedItemInteractor {
    interface Callback {
        void onFeedItemRetrieved(FeedItem feedItem);
        void onFeedItemRetrieveFailed(Throwable t);
    }
}
