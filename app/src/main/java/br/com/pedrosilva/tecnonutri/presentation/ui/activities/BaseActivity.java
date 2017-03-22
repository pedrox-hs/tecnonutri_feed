package br.com.pedrosilva.tecnonutri.presentation.ui.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.presentation.navigation.Navigator;
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.FeedItemClickListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ProfileClickListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener;

/**
 * Created by psilva on 3/17/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements FeedItemClickListener, ProfileClickListener, BaseView {
    private View progressBar;
    private View llError;
    private TextView tvErrorMessage;

    private boolean hasProgress = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClickView(FeedItem feedItem) {
        Navigator.navigateToFeedItemDetails(this, feedItem.getFeedHash(), feedItem.getDate());
    }

    @Override
    public void onClickView(Profile profile) {
        Navigator.navigateToProfile(this, profile.getId(), profile.getName());
    }

    @Override
    public void showError(String message) {
        Snackbar.make(getContentView(), message, Snackbar.LENGTH_LONG).show();
        if (hasProgress) {
            tvErrorMessage.setText(message);
            llError.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress() {
        View hideOnProgress = getContentView().findViewWithTag("hide_on_progress");
        if (hideOnProgress != null)
            hideOnProgress.setVisibility(View.GONE);
        View showOnProgress = getContentView().findViewWithTag("show_on_progress");
        if (showOnProgress != null)
            showOnProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        View hideOnProgress = getContentView().findViewWithTag("hide_on_progress");
        if (hideOnProgress != null)
            hideOnProgress.setVisibility(View.VISIBLE);
        View showOnProgress = getContentView().findViewWithTag("show_on_progress");
        if (showOnProgress != null)
            showOnProgress.setVisibility(View.GONE);
    }

    protected View getContentView() {
        return findViewById(android.R.id.content);
    }

    protected void bindProgress() {
        View v = findViewById(R.id.rl_progress);
        if (v != null) {
            hasProgress = true;
            progressBar = v.findViewById(R.id.progress_bar);
            llError = v.findViewById(R.id.ll_error);
            tvErrorMessage = (TextView) v.findViewById(R.id.tv_error_message);
            llError.setOnClickListener(new SingleOnClickListener() {
                @Override
                public void onItemClick(View view) {
                    llError.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    reloadAll();
                }
            });
        }
    }

    void reloadAll() {

    }

}
