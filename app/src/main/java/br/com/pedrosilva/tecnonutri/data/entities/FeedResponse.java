package br.com.pedrosilva.tecnonutri.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by psilva on 3/16/17.
 */

public class FeedResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("t")
    private int timestamp;

    @SerializedName("p")
    private int page;

    @SerializedName("items")
    private ArrayList<FeedItem> items;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<FeedItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<FeedItem> items) {
        this.items = items;
    }
}
