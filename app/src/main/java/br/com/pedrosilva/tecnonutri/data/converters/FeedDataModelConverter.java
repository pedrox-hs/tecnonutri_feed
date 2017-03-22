package br.com.pedrosilva.tecnonutri.data.converters;

import java.util.ArrayList;
import java.util.List;

import br.com.pedrosilva.tecnonutri.data.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.data.entities.Food;

/**
 * Created by psilva on 3/16/17.
 */

public abstract class FeedDataModelConverter {

    public static FeedItem convertToDataModel(br.com.pedrosilva.tecnonutri.domain.entities.FeedItem feedItem) {
        FeedItem result = new FeedItem();

        result.setFeedHash(feedItem.getFeedHash());
        result.setImageUrl(feedItem.getImageUrl());
        result.setProfile(ProfileDataModelConverter.convertToDataModel(feedItem.getProfile()));
        result.setDate(feedItem.getDate());
        result.setMeal(MealTypeDataModelConverter.convertToDataModel(feedItem.getMeal()));
        result.setLiked(feedItem.isLiked());

        result.setCarbohydrate(feedItem.getCarbohydrate());
        result.setEnergy(feedItem.getEnergy());
        result.setFat(feedItem.getFat());
        result.setProtein(feedItem.getProtein());
        List<br.com.pedrosilva.tecnonutri.domain.entities.Food> foods = feedItem.getFoods();
        if (foods != null) {
            result.setFoods(FoodDataModelConverter.convertListToDataModel(foods));
        }

        return result;
    }

    public static br.com.pedrosilva.tecnonutri.domain.entities.FeedItem convertToDomainModel(FeedItem feedItem) {
        br.com.pedrosilva.tecnonutri.domain.entities.FeedItem result = new br.com.pedrosilva.tecnonutri.domain.entities.FeedItem();

        result.setFeedHash(feedItem.getFeedHash());
        result.setImageUrl(feedItem.getImageUrl());
        result.setProfile(ProfileDataModelConverter.convertToDomainModel(feedItem.getProfile()));
        result.setDate(feedItem.getDate());
        result.setMeal(MealTypeDataModelConverter.convertToDomainModel(feedItem.getMeal()));
        result.setLiked(feedItem.isLiked());

        result.setCarbohydrate(feedItem.getCarbohydrate());
        result.setEnergy(feedItem.getEnergy());
        result.setFat(feedItem.getFat());
        result.setProtein(feedItem.getProtein());
        List<Food> foods = feedItem.getFoods();
        if (foods != null) {
            result.setFoods(FoodDataModelConverter.convertListToDomainModel(foods));
        }

        return result;
    }

    public static List<FeedItem> convertListToDataModel(List<br.com.pedrosilva.tecnonutri.domain.entities.FeedItem> feedItems) {
        List<FeedItem> result = new ArrayList<>();

        for (br.com.pedrosilva.tecnonutri.domain.entities.FeedItem feedItem : feedItems) {
            result.add(convertToDataModel(feedItem));
        }

        // cleanup
        feedItems.clear();
        feedItems = null;

        return result;
    }

    public static List<br.com.pedrosilva.tecnonutri.domain.entities.FeedItem> convertListToDomainModel(List<FeedItem> feedItems) {
        List<br.com.pedrosilva.tecnonutri.domain.entities.FeedItem> result = new ArrayList<>();

        for (FeedItem feedItem : feedItems) {
            result.add(convertToDomainModel(feedItem));
        }

        // cleanup
        feedItems.clear();
        feedItems = null;

        return result;
    }
}
