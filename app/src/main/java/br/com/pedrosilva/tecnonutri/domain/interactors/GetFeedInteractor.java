package br.com.pedrosilva.tecnonutri.domain.interactors;


import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor;


public interface GetFeedInteractor extends Interactor {

    interface Callback {
        void onFeedRetrieved(List<FeedItem> feedItems, int page, int timestamp);
        void onFeedRetrieveFailed(Throwable t);
    }
}
