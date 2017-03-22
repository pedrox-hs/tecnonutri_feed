package br.com.pedrosilva.tecnonutri.data.entities;

/**
 * Created by psilva on 3/17/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by psilva on 3/16/17.
 */
public class ProfileResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("t")
    private int timestamp;

    @SerializedName("p")
    private int page;

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("items")
    private List<FeedItem> items;

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    public void setItems(List<FeedItem> items) {
        this.items = items;
    }
}