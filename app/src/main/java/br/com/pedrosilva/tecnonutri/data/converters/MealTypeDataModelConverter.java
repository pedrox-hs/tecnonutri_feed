package br.com.pedrosilva.tecnonutri.data.converters;

import br.com.pedrosilva.tecnonutri.data.entities.MealType;

/**
 * Created by psilva on 3/16/17.
 */

public class MealTypeDataModelConverter {

    public static MealType convertToDataModel(br.com.pedrosilva.tecnonutri.domain.entities.MealType mealType) {
        return MealType.valueOf(mealType.name());
    }

    public static br.com.pedrosilva.tecnonutri.domain.entities.MealType convertToDomainModel(MealType mealType) {
        return br.com.pedrosilva.tecnonutri.domain.entities.MealType.valueOf(mealType.name());
    }
}
