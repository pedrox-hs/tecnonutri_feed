package br.com.pedrosilva.tecnonutri.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by psilva on 3/16/17.
 */

public class FeedItem {

    @SerializedName("feedHash")
    private String feedHash;

    @SerializedName("profile")
    private Profile profile;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("meal")
    private MealType meal;

    @SerializedName("date")
    private Date date;

    @SerializedName("energy")
    private Double energy;

    @SerializedName("carbohydrate")
    private Double carbohydrate;

    @SerializedName("fat")
    private Double fat;

    @SerializedName("protein")
    private Double protein;

    @SerializedName("foods")
    private List<Food> foods;

    @SerializedName("liked")
    private boolean liked;

    public String getFeedHash() {
        return feedHash;
    }

    public void setFeedHash(String feedHash) {
        this.feedHash = feedHash;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MealType getMeal() {
        return meal;
    }

    public void setMeal(MealType meal) {
        this.meal = meal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
