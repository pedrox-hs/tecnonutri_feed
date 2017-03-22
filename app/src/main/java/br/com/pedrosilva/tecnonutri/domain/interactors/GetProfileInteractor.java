package br.com.pedrosilva.tecnonutri.domain.interactors;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;

/**
 * Created by psilva on 3/18/17.
 */

public interface GetProfileInteractor {
    interface Callback {
        void onProfileRetrieved(Profile profile, List<FeedItem> feedItems, int page, int timestamp);
        void onProfileRetrieveFailed(Throwable t);
    }
}
