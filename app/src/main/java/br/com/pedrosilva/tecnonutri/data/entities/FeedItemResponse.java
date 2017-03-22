package br.com.pedrosilva.tecnonutri.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psilva on 3/16/17.
 */

public class FeedItemResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("item")
    private FeedItem item;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public FeedItem getItem() {
        return item;
    }

    public void setItem(FeedItem item) {
        this.item = item;
    }
}
