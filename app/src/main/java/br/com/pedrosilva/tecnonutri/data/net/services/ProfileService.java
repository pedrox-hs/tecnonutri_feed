package br.com.pedrosilva.tecnonutri.data.net.services;

import br.com.pedrosilva.tecnonutri.data.entities.ProfileResponse;
import br.com.pedrosilva.tecnonutri.data.net.ApiConstants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by psilva on 3/16/17.
 */
public interface ProfileService {
    @GET(ApiConstants.PROFILE)
    Call<ProfileResponse> profile(@Path("id") int id);

    @GET(ApiConstants.PROFILE)
    Call<ProfileResponse> posts(@Path("id") int id, @Query("p") int page, @Query("t") int timestamp);
}
