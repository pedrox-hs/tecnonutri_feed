package br.com.pedrosilva.tecnonutri.data.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by psilva on 3/16/17.
 */

public class LikedFeedItem extends RealmObject {
    @PrimaryKey
    private String feedHash;

    public LikedFeedItem() {
    }

    public LikedFeedItem(String feedHash) {
        this.feedHash = feedHash;
    }

    public String getFeedHash() {
        return feedHash;
    }

    public void setFeedHash(String feedHash) {
        this.feedHash = feedHash;
    }
}
