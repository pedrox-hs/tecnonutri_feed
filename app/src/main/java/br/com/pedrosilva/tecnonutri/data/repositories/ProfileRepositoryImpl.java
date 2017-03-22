package br.com.pedrosilva.tecnonutri.data.repositories;

import android.content.Context;

import java.util.List;

import br.com.pedrosilva.tecnonutri.data.converters.FeedDataModelConverter;
import br.com.pedrosilva.tecnonutri.data.converters.ProfileDataModelConverter;
import br.com.pedrosilva.tecnonutri.data.entities.ProfileResponse;
import br.com.pedrosilva.tecnonutri.data.net.ApiServiceGenerator;
import br.com.pedrosilva.tecnonutri.data.net.services.ProfileService;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.domain.repositories.ProfileRepository;
import retrofit2.Response;

/**
 * Created by psilva on 3/18/17.
 */

public class ProfileRepositoryImpl implements ProfileRepository {
    private Context context;

    public ProfileRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public void get(int userId, ProfileCallback callback) {
        ProfileService profileService = ApiServiceGenerator.getService(ProfileService.class);
        try {
            Response<ProfileResponse> response = profileService.profile(userId).execute();
            if (response.isSuccessful()) {
                ProfileResponse profileResponse = response.body();
                Profile profile = ProfileDataModelConverter.convertToDomainModel(profileResponse.getProfile());
                List<FeedItem> feedItems = FeedDataModelConverter.convertListToDomainModel(profileResponse.getItems());
                callback.onSuccess(profile, feedItems, profileResponse.getPage(), profileResponse.getTimestamp());
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void loadMorePosts(int userId, int page, int timestamp, ProfileCallback callback) {
        ProfileService profileService = ApiServiceGenerator.getService(ProfileService.class);
        try {
            Response<ProfileResponse> response = profileService.posts(userId, page, timestamp).execute();
            if (response.isSuccessful()) {
                ProfileResponse profileResponse = response.body();
                Profile profile = ProfileDataModelConverter.convertToDomainModel(profileResponse.getProfile());
                List<FeedItem> feedItems = FeedDataModelConverter.convertListToDomainModel(profileResponse.getItems());
                callback.onSuccess(profile, feedItems, profileResponse.getPage(), profileResponse.getTimestamp());
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
