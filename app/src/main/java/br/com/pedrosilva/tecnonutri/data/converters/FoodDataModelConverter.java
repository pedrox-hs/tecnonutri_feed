package br.com.pedrosilva.tecnonutri.data.converters;

import java.util.ArrayList;
import java.util.List;

import br.com.pedrosilva.tecnonutri.data.entities.Food;

/**
 * Created by psilva on 3/16/17.
 */

public abstract class FoodDataModelConverter {

    public static Food convertToDataModel(br.com.pedrosilva.tecnonutri.domain.entities.Food food) {
        Food result = new Food();

        result.setDescription(food.getDescription());
        result.setMeasure(food.getMeasure());
        result.setAmount(food.getAmount());
        result.setWeight(food.getWeight());

        result.setCarbohydrate(food.getCarbohydrate());
        result.setEnergy(food.getEnergy());
        result.setFat(food.getFat());
        result.setProtein(food.getProtein());

        return result;
    }

    public static br.com.pedrosilva.tecnonutri.domain.entities.Food convertToDomainModel(Food food) {
        br.com.pedrosilva.tecnonutri.domain.entities.Food result = new br.com.pedrosilva.tecnonutri.domain.entities.Food();

        result.setDescription(food.getDescription());
        result.setMeasure(food.getMeasure());
        result.setAmount(food.getAmount());
        result.setWeight(food.getWeight());

        result.setCarbohydrate(food.getCarbohydrate());
        result.setEnergy(food.getEnergy());
        result.setFat(food.getFat());
        result.setProtein(food.getProtein());

        return result;
    }

    public static List<Food> convertListToDataModel(List<br.com.pedrosilva.tecnonutri.domain.entities.Food> foods) {
        List<Food> result = new ArrayList<>();

        for (br.com.pedrosilva.tecnonutri.domain.entities.Food food : foods) {
            result.add(convertToDataModel(food));
        }

        // cleanup
        foods.clear();
        foods = null;

        return result;
    }

    public static List<br.com.pedrosilva.tecnonutri.domain.entities.Food> convertListToDomainModel(List<Food> foods) {
        List<br.com.pedrosilva.tecnonutri.domain.entities.Food> result = new ArrayList<>();

        for (Food food : foods) {
            result.add(convertToDomainModel(food));
        }

        // cleanup
        foods.clear();
        foods = null;

        return result;
    }
}
