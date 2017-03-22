package br.com.pedrosilva.tecnonutri.data.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import br.com.pedrosilva.tecnonutri.BuildConfig;
import br.com.pedrosilva.tecnonutri.data.net.json.GsonUTCDateAdapter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by psilva on 3/16/17.
 */

public class ApiServiceGenerator {

    private static Retrofit.Builder builder = new Retrofit.Builder();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    static {

        Gson gson = new GsonBuilder()
                // http://stackoverflow.com/questions/26044881/java-date-to-utc-using-gson
                .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                .create();

        if (BuildConfig.DEBUG) {
            // enable logging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        builder.baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    public static <T> T getService(Class<T> serviceClass) {
        return builder.build().create(serviceClass);
    }
}
