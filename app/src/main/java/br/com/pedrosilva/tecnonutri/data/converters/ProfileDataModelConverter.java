package br.com.pedrosilva.tecnonutri.data.converters;

import br.com.pedrosilva.tecnonutri.data.entities.Profile;

/**
 * Created by psilva on 3/16/17.
 */

public class ProfileDataModelConverter {

    public static Profile convertToDataModel(br.com.pedrosilva.tecnonutri.domain.entities.Profile profile) {
        Profile result = new Profile();

        result.setId(profile.getId());
        result.setName(profile.getName());
        result.setImageUrl(profile.getImageUrl());
        result.setGeneralGoal(profile.getGeneralGoal());

        return result;
    }

    public static br.com.pedrosilva.tecnonutri.domain.entities.Profile convertToDomainModel(Profile profile) {
        br.com.pedrosilva.tecnonutri.domain.entities.Profile result = new br.com.pedrosilva.tecnonutri.domain.entities.Profile();

        result.setId(profile.getId());
        result.setName(profile.getName());
        result.setImageUrl(profile.getImageUrl());
        result.setGeneralGoal(profile.getGeneralGoal());

        return result;
    }
}
