package br.com.pedrosilva.tecnonutri.presentation.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Profile;
import br.com.pedrosilva.tecnonutri.presentation.ui.components.CircleTransformation;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ChangeLikeListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.FeedItemClickListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.ProfileClickListener;
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener;
import br.com.pedrosilva.tecnonutri.util.AppUtil;

/**
 * Created by psilva on 3/16/17.
 */

public class FeedAdapter extends GenericAdapter<FeedItem, RecyclerView.ViewHolder> {

    private FeedItemClickListener feedItemClickListener;
    private ProfileClickListener profileClickListener;
    private ChangeLikeListener changeLikeListener;

    public FeedAdapter(Context context) {
        super(context, true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEWTYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_feed, parent, false);
            viewHolder = new ViewHolder(
                    view,
                    feedItemClickListener,
                    profileClickListener,
                    changeLikeListener
            );
        } else {
            View view = inflater.inflate(R.layout.progress, parent, false);
            viewHolder = new ViewHolderLoader(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEWTYPE_ITEM) {
            final FeedItem item = getItem(position);
            ((ViewHolder) holder).setup(item);
        } else {
            ((ViewHolderLoader) holder).setup();
        }
    }

    public void setFeedItemClickListener(FeedItemClickListener feedItemClickListener) {
        this.feedItemClickListener = feedItemClickListener;
    }

    public void setProfileClickListener(ProfileClickListener profileClickListener) {
        this.profileClickListener = profileClickListener;
    }

    public void setChangeLikeListener(ChangeLikeListener changeLikeListener) {
        this.changeLikeListener = changeLikeListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final FeedItemClickListener feedItemClickListener;
        private final ProfileClickListener profileClickListener;
        private final ChangeLikeListener changeLikeListener;

        private TextView tvProfileName;
        private ImageView ivProfileImage;
        private TextView tvProfileGeneralGoal;
        private ImageView ivMeal;
        private ProgressBar pbLoadingImage;
        private TextView tvMealDate;
        private TextView tvEnergy;
        private RelativeLayout rlHeader;
        private CheckBox cbLike;

        ViewHolder(final View v, FeedItemClickListener feedItemClickListener, ProfileClickListener profileClickListener, ChangeLikeListener changeLikeListener) {
            super(v);

            this.feedItemClickListener = feedItemClickListener;
            this.profileClickListener = profileClickListener;
            this.changeLikeListener = changeLikeListener;

            bindElements(v);
            v.setOnClickListener(new FeedItemClick(feedItemClickListener, this));
        }

        private void bindElements(View v) {
            tvProfileName = (TextView) v.findViewById(R.id.tv_profile_name);
            ivProfileImage = (ImageView) v.findViewById(R.id.iv_profile_image);
            tvProfileGeneralGoal = (TextView) v.findViewById(R.id.tv_profile_general_goal);
            ivMeal = (ImageView) v.findViewById(R.id.iv_meal);
            pbLoadingImage = (ProgressBar) v.findViewById(R.id.pb_loading_image);
            tvMealDate = (TextView) v.findViewById(R.id.tv_meal_date);
            tvEnergy = (TextView) v.findViewById(R.id.tv_energy);
            rlHeader = (RelativeLayout) v.findViewById(R.id.rl_header);
            cbLike = (CheckBox) v.findViewById(R.id.cb_like);
        }

        public void setup(final FeedItem feedItem) {
            final Context context = itemView.getContext();
            final Resources resources = context.getResources();

            cbLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    if (feedItem != null && changeLikeListener != null) {
                        changeLikeListener.onChange(feedItem.getFeedHash(), checked);
                    }
                }
            });
            rlHeader.setOnClickListener(new ProfileClick(profileClickListener, this));

            ivProfileImage.setImageResource(R.drawable.profile_image_placeholder);
            ivMeal.setImageDrawable(null);
            pbLoadingImage.setVisibility(View.VISIBLE);

            Profile profile = feedItem.getProfile();

            cbLike.setChecked(feedItem.isLiked());

            tvProfileName.setText(profile.getName());
            tvProfileGeneralGoal.setText(profile.getGeneralGoal());

            String dateFormatted = AppUtil.formatDate(feedItem.getDate());
            String mealDate = resources.getString(R.string.meal_date, dateFormatted);
            tvMealDate.setText(mealDate);

            String kcal = resources.getString(R.string.qty_energy, AppUtil.formatKcal(feedItem.getEnergy()));
            tvEnergy.setText(kcal);

            Picasso.with(context)
                    .load(profile.getImageUrl())
                    .placeholder(R.drawable.profile_image_placeholder)
                    .error(R.drawable.profile_image_placeholder)
                    .transform(new CircleTransformation())
                    .fit()
                    .centerCrop()
                    .into(ivProfileImage);

            Picasso.with(context)
                    .load(feedItem.getImageUrl())
                    .fit()
                    .centerCrop()
                    .into(ivMeal, new Callback() {
                        @Override
                        public void onSuccess() {
                            pbLoadingImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }

    private class FeedItemClick extends SingleOnClickListener {

        private final ViewHolder vh;
        private final FeedItemClickListener listener;

        FeedItemClick(FeedItemClickListener listener, ViewHolder vh) {
            this.listener = listener;
            this.vh = vh;
        }

        @Override
        public void onItemClick(View view) {
            if (listener != null) {
                FeedItem feedItem = getItem(vh.getAdapterPosition());
                if (feedItem != null) {
                    listener.onClickView(feedItem);
                }
            }
        }
    }

    private class ProfileClick extends SingleOnClickListener {

        private final ViewHolder vh;
        private final ProfileClickListener listener;

        ProfileClick(ProfileClickListener listener, ViewHolder vh) {
            this.listener = listener;
            this.vh = vh;
        }

        @Override
        public void onItemClick(View view) {
            if (listener != null) {
                FeedItem feedItem = getItem(vh.getAdapterPosition());
                if (feedItem != null) {
                    Profile profile = feedItem.getProfile();
                    listener.onClickView(profile);
                }
            }
        }
    }
}
