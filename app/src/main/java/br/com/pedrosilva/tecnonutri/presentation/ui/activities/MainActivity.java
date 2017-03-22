package br.com.pedrosilva.tecnonutri.presentation.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.data.repositories.FeedRepositoryImpl;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.executor.impl.ThreadExecutor;
import br.com.pedrosilva.tecnonutri.presentation.presenters.FeedPresenter;
import br.com.pedrosilva.tecnonutri.presentation.presenters.impl.FeedPresenterImpl;
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.FeedAdapter;
import br.com.pedrosilva.tecnonutri.presentation.ui.adapters.GenericAdapter;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ChangeLikeListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.EndlessRecyclerViewScrollListener;
import br.com.pedrosilva.tecnonutri.threading.MainThreadImpl;

/**
 * Created by psilva on 3/19/17.
 */

public class MainActivity extends BaseActivity implements FeedPresenter.View {
    private static final String TAG = MainActivity.class.getSimpleName();
    private FeedPresenter feedPresenter;
    private RecyclerView rvFeed;
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayoutManager linearLayoutManager;
    private int timestamp = 0;
    private int nextPage = 0;
    private boolean isFirstLoad = true;
    private boolean isEndList = false;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindElements();

        init();
    }

    private void init() {
        setupRecyclerView();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedPresenter.refresh();
            }
        });

        feedPresenter = new FeedPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new FeedRepositoryImpl(this)
        );
    }

    private void setupRecyclerView() {
        feedAdapter = new FeedAdapter(this);
        feedAdapter.setFeedItemClickListener(this);
        feedAdapter.setProfileClickListener(this);
        feedAdapter.setChangeLikeListener(new ChangeLikeListener() {
            @Override
            public void onChange(String feedHash, boolean liked) {
                feedPresenter.changeLike(feedHash, liked);
            }
        });
        feedAdapter.setRetryClickListener(new GenericAdapter.RetryClickListener() {
            @Override
            public void onRetry() {
                if (isFirstLoad) {
                    feedPresenter.load();
                } else {
                    feedPresenter.loadMore(nextPage, timestamp);
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(linearLayoutManager);
        rvFeed.setHasFixedSize(true);
        rvFeed.setAdapter(feedAdapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                feedPresenter.loadMore(nextPage, timestamp);
            }
        };
    }

    private void bindElements() {
        rvFeed = (RecyclerView) findViewById(R.id.rv_feed);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        feedPresenter.resume();

        for (FeedItem item : feedAdapter.getItems()) {
            item.setLiked(feedPresenter.isLiked(item.getFeedHash()));
        }

        feedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFeed(List<FeedItem> feedItems, int page, int timestamp) {
        this.timestamp = timestamp;
        this.nextPage = page + 1;
        if (isFirstLoad || swipeRefresh.isRefreshing()) {
            isFirstLoad = false;
            feedAdapter.setItems(feedItems);
            swipeRefresh.setRefreshing(false);
            isEndList = false;
            endlessRecyclerViewScrollListener.resetState();
            rvFeed.clearOnScrollListeners();
            rvFeed.addOnScrollListener(endlessRecyclerViewScrollListener);
        } else {
            feedAdapter.appendItems(feedItems);
        }
        isEndList = feedItems.size() == 0;
    }

    @Override
    public void onLoadFail(Throwable t) {
        swipeRefresh.setRefreshing(false);
        String msgError = getString(R.string.fail_to_load_try_again);
        feedAdapter.notifyError(msgError);
        showError(msgError);
    }

    @Override
    public void onChangeLike(String feedHash, boolean like) {

    }
}
