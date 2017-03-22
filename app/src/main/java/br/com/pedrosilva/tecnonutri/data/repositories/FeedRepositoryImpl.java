package br.com.pedrosilva.tecnonutri.data.repositories;

import android.content.Context;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import br.com.pedrosilva.tecnonutri.data.converters.FeedDataModelConverter;
import br.com.pedrosilva.tecnonutri.data.entities.FeedItemResponse;
import br.com.pedrosilva.tecnonutri.data.entities.FeedResponse;
import br.com.pedrosilva.tecnonutri.data.entities.LikedFeedItem;
import br.com.pedrosilva.tecnonutri.data.net.ApiServiceGenerator;
import br.com.pedrosilva.tecnonutri.data.net.services.FeedService;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by psilva on 3/16/17.
 */

public class FeedRepositoryImpl implements FeedRepository {
    private static final String TAG = FeedRepositoryImpl.class.getName();

    private Context context;

    public FeedRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public void get(String feedHash, FeedItemCallback callback) {
        FeedService service = ApiServiceGenerator.getService(FeedService.class);
        try {
            Response<FeedItemResponse> feedItemResponse = service.item(feedHash).execute();
            if (feedItemResponse.isSuccessful()) {
                FeedItem feedItem = FeedDataModelConverter.convertToDomainModel(feedItemResponse.body().getItem());
                checkLikedAsync(feedItem, new Callback(callback));
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void likeItem(final String feedHash) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(new LikedFeedItem(feedHash));
            }
        });
        realm.close();
    }

    @Override
    public void dislikeItem(final String feedHash) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(LikedFeedItem.class)
                        .equalTo("feedHash", feedHash)
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public boolean isLiked(String feedHash) {
        Realm realm = Realm.getDefaultInstance();
        try {
            LikedFeedItem likedFeedItem = realm.where(LikedFeedItem.class)
                    .equalTo("feedHash", feedHash)
                    .findFirst();
            return likedFeedItem != null;
        } finally {
            realm.close();
        }
    }

    private void checkLikedAsync(final List<FeedItem> items, final Callback callback) {
        if (items.size() == 0) {
            callback.onResult(items);
            return;
        }

        final String[] feedHashes = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            feedHashes[i] = items.get(i).getFeedHash();
        }

        new Thread() {
            private Realm realm;

            @Override
            public void run() {
                Looper.prepare();
                try {
                    realm = Realm.getDefaultInstance();
                    RealmResults<LikedFeedItem> likedFeedItems = realm.where(LikedFeedItem.class)
                            .in("feedHash", feedHashes)
                            .findAllAsync();
                    likedFeedItems.load();

                    for (LikedFeedItem likedFeedItem : likedFeedItems) {
                        for (FeedItem item : items) {
                            if (item.getFeedHash().equals(likedFeedItem.getFeedHash())) {
                                item.setLiked(true);
                            }
                        }
                    }
                    callback.onResult(items);
                    Looper.loop();
                } catch (Throwable t) {
                    callback.onError(t);
                } finally {
                    realm.close();
                }
            }
        }.start();
    }

    private void checkLikedAsync(FeedItem item, Callback callback) {
        ArrayList<FeedItem> items = new ArrayList<>();
        items.add(item);
        checkLikedAsync(items, callback);
    }

    @Override
    public void firstList(FeedCallback callback) {
        FeedService feedService = ApiServiceGenerator.getService(FeedService.class);
        try {
            Call<FeedResponse> call = feedService.list();
            Response<FeedResponse> response = call.execute();
            if (response.isSuccessful()) {
                FeedResponse feedResponse = response.body();
                List<FeedItem> feedItems = FeedDataModelConverter.convertListToDomainModel(feedResponse.getItems());
                checkLikedAsync(feedItems, new Callback(callback, feedResponse.getPage(), feedResponse.getTimestamp()));
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void loadMore(int page, int timestamp, FeedCallback callback) {
        FeedService feedService = ApiServiceGenerator.getService(FeedService.class);
        try {
            Call<FeedResponse> call = feedService.paginate(page, timestamp);
            Response<FeedResponse> response = call.execute();
            if (response.isSuccessful()) {
                FeedResponse feedResponse = response.body();
                List<FeedItem> feedItems = FeedDataModelConverter.convertListToDomainModel(feedResponse.getItems());
                checkLikedAsync(feedItems, new Callback(callback, feedResponse.getPage(), feedResponse.getTimestamp()));
            }
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    private class Callback {

        private int timestamp;
        private int page;

        private FeedCallback feedCallback;
        private FeedItemCallback feedItemCallback;

        Callback(FeedCallback callback, int page, int timestamp) {
            this.feedCallback = callback;
            this.page = page;
            this.timestamp = timestamp;
        }

        Callback(FeedItemCallback callback) {
            this.feedItemCallback = callback;
        }

        void onResult(List<FeedItem> feedItems) {
            if (feedCallback != null) {
                feedCallback.onSuccess(feedItems, page, timestamp);
            } else {
                feedItemCallback.onSuccess(feedItems.get(0));
            }
        }

        void onError(Throwable t) {
            if (feedCallback != null) {
                feedCallback.onError(t);
            } else {
                feedItemCallback.onError(t);
            }
        }

    }
}
