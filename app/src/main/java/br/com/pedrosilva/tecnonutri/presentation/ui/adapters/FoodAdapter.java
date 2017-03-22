package br.com.pedrosilva.tecnonutri.presentation.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.pedrosilva.tecnonutri.R;
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem;
import br.com.pedrosilva.tecnonutri.domain.entities.Food;
import br.com.pedrosilva.tecnonutri.util.AppUtil;

/**
 * Created by psilva on 3/21/17.
 */

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private final int VIEW_TYPE_FOOD = 0;
    private final int VIEW_TYPE_TOTAL = 1;

    private FeedItem feedItem;

    public FoodAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        RecyclerView.ViewHolder holder;
        View view;

        if (viewType == VIEW_TYPE_FOOD) {
            view = inflater.inflate(R.layout.item_food, parent, false);
            holder = new ViewHolderFood(view);
        } else {
            view = inflater.inflate(R.layout.item_food_total, parent, false);
            holder = new ViewHolderTotal(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_FOOD:
                ViewHolderFood holderFood = (ViewHolderFood) holder;
                holderFood.setup(getItem(position));
                break;
            case VIEW_TYPE_TOTAL:
                ViewHolderTotal holderTotal = (ViewHolderTotal) holder;
                holderTotal.setup(feedItem);
                break;
        }
    }

    private Food getItem(int position) {
        if (feedItem != null) {
            return feedItem.getFoods().get(position);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 1) {
            return VIEW_TYPE_FOOD;
        }
        return VIEW_TYPE_TOTAL;
    }

    public void setFoodItem(FeedItem feedItem) {
        this.feedItem = feedItem;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (feedItem != null) {
            return feedItem.getFoods().size() + 1;
        }
        return 0;
    }

    private class ViewHolderFood extends RecyclerView.ViewHolder {

        private TextView tvDescription;
        private TextView tvQty;

        private TextView tvNutritionalEnergy;
        private TextView tvNutritionalCarbohydrate;
        private TextView tvNutritionalProtein;
        private TextView tvNutritionalFat;

        public ViewHolderFood(View v) {
            super(v);
            bindElements(v);
        }

        private void bindElements(View v) {
            tvDescription = (TextView) v.findViewById(R.id.tv_description);
            tvQty = (TextView) v.findViewById(R.id.tv_qty);

            tvNutritionalEnergy = (TextView) v.findViewById(R.id.tv_nutritional_energy);
            tvNutritionalCarbohydrate = (TextView) v.findViewById(R.id.tv_nutritional_carbohydrate);
            tvNutritionalProtein = (TextView) v.findViewById(R.id.tv_nutritional_protein);
            tvNutritionalFat = (TextView) v.findViewById(R.id.tv_nutritional_fat);
        }

        private void setup(Food food) {
            final Context context = itemView.getContext();
            final Resources resources = context.getResources();

            tvDescription.setText(food.getDescription());

            String qty = resources.getString(R.string.qty_food, AppUtil.formatDecimal(food.getAmount()), food.getMeasure(), AppUtil.formatWeight(food.getWeight()));
            tvQty.setText(qty);

            String kcal = resources.getString(R.string.qty_energy, AppUtil.formatKcal(food.getEnergy()));
            tvNutritionalEnergy.setText(kcal);

            String carb = resources.getString(R.string.weight, AppUtil.formatWeight(food.getCarbohydrate()));
            tvNutritionalCarbohydrate.setText(carb);

            String prot = resources.getString(R.string.weight, AppUtil.formatWeight(food.getProtein()));
            tvNutritionalProtein.setText(prot);

            String fat = resources.getString(R.string.weight, AppUtil.formatWeight(food.getFat()));
            tvNutritionalFat.setText(fat);
        }
    }

    private class ViewHolderTotal extends RecyclerView.ViewHolder {

        private TextView tvNutritionalEnergy;
        private TextView tvNutritionalCarbohydrate;
        private TextView tvNutritionalProtein;
        private TextView tvNutritionalFat;

        public ViewHolderTotal(View v) {
            super(v);
            bindElements(v);
        }

        private void bindElements(View v) {
            tvNutritionalEnergy = (TextView) v.findViewById(R.id.tv_nutritional_energy);
            tvNutritionalCarbohydrate = (TextView) v.findViewById(R.id.tv_nutritional_carbohydrate);
            tvNutritionalProtein = (TextView) v.findViewById(R.id.tv_nutritional_protein);
            tvNutritionalFat = (TextView) v.findViewById(R.id.tv_nutritional_fat);
        }

        private void setup(FeedItem feedItem) {
            final Context context = itemView.getContext();
            final Resources resources = context.getResources();

            String kcal = resources.getString(R.string.qty_energy, AppUtil.formatKcal(feedItem.getEnergy()));
            tvNutritionalEnergy.setText(kcal);

            String carb = resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.getCarbohydrate()));
            tvNutritionalCarbohydrate.setText(carb);

            String prot = resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.getProtein()));
            tvNutritionalProtein.setText(prot);

            String fat = resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.getFat()));
            tvNutritionalFat.setText(fat);
        }
    }
}
