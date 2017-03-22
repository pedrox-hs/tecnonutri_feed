package br.com.pedrosilva.tecnonutri.data.net.services;

import android.support.annotation.NonNull;

import br.com.pedrosilva.tecnonutri.data.entities.FeedItemResponse;
import br.com.pedrosilva.tecnonutri.data.entities.FeedResponse;
import br.com.pedrosilva.tecnonutri.data.net.ApiConstants;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by psilva on 3/16/17.
 */

public interface FeedService {
    @GET(ApiConstants.FEED)
    Call<FeedResponse> list();

    @GET(ApiConstants.FEED)
    Call<FeedResponse> paginate(@Query("p") int page, @Query("t") int timestamp);

    @GET(ApiConstants.FEED_ITEM)
    Call<FeedItemResponse> item(@NonNull @Path("feedHash") String feedHash);
}
