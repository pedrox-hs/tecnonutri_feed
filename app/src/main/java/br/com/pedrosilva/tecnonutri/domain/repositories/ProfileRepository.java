package br.com.pedrosilva.tecnonutri.domain.repositories;

import java.util.List;

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;

/**
 * Created by psilva on 3/18/17.
 */

public interface ProfileRepository {
    void get(int userId, ProfileCallback callback);
    void loadMorePosts(int userId, int page, int timestamp, ProfileCallback callback);

    interface ProfileCallback {
        void onSuccess(Profile profile, List<FeedItem> feedItems, int page, int timestamp);
        void onError(Throwable t);
    }
}
