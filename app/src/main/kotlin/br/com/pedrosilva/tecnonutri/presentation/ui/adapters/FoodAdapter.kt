package br.com.pedrosilva.tecnonutri.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Food
import br.com.pedrosilva.tecnonutri.util.AppUtil

class FoodAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var feedItem: FeedItem? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val items: List<Food>
        get() = feedItem?.foods ?: listOf()

    companion object {
        private const val VIEW_TYPE_FOOD = 0
        private const val VIEW_TYPE_TOTAL = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder: RecyclerView.ViewHolder
        val view: View
        if (viewType == VIEW_TYPE_FOOD) {
            view = inflater.inflate(R.layout.item_food, parent, false)
            holder = ViewHolderFood(view)
        } else {
            view = inflater.inflate(R.layout.item_food_total, parent, false)
            holder = ViewHolderTotal(view)
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            VIEW_TYPE_FOOD -> {
                val holderFood = holder as ViewHolderFood
                holderFood.setup(getItem(position))
            }
            VIEW_TYPE_TOTAL -> feedItem?.let { item ->
                val holderTotal = holder as ViewHolderTotal
                holderTotal.setup(item)
            }
        }
    }

    private fun getItem(position: Int): Food = items[position]

    override fun getItemViewType(position: Int): Int =
        if (position < itemCount - 1) VIEW_TYPE_FOOD
        else VIEW_TYPE_TOTAL

    override fun getItemCount(): Int =
        if (items.isNotEmpty()) items.size + 1
        else 0

    private inner class ViewHolderFood(view: View) : RecyclerView.ViewHolder(view) {

        private var tvDescription: TextView = view.findViewById(R.id.tv_description)
        private var tvQty: TextView = view.findViewById(R.id.tv_qty)
        private var tvNutritionalEnergy: TextView = view.findViewById(R.id.tv_nutritional_energy)
        private var tvNutritionalCarbohydrate: TextView =
            view.findViewById(R.id.tv_nutritional_carbohydrate)
        private var tvNutritionalProtein: TextView = view.findViewById(R.id.tv_nutritional_protein)
        private var tvNutritionalFat: TextView = view.findViewById(R.id.tv_nutritional_fat)

        fun setup(food: Food) {
            val resources = itemView.context.resources
            tvDescription.text = food.description
            tvQty.text = resources.getString(
                R.string.qty_food,
                AppUtil.formatDecimal(food.amount!!),
                food.measure,
                AppUtil.formatWeight(food.weight!!)
            )
            tvNutritionalEnergy.text =
                resources.getString(R.string.qty_energy, AppUtil.formatKcal(food.energy!!))
            tvNutritionalCarbohydrate.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(food.carbohydrate!!))
            tvNutritionalProtein.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(food.protein!!))
            tvNutritionalFat.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(food.fat!!))
        }
    }

    private inner class ViewHolderTotal(view: View) : RecyclerView.ViewHolder(view) {

        private val tvNutritionalEnergy: TextView = view.findViewById(R.id.tv_nutritional_energy)
        private val tvNutritionalCarbohydrate: TextView =
            view.findViewById(R.id.tv_nutritional_carbohydrate)
        private val tvNutritionalProtein: TextView = view.findViewById(R.id.tv_nutritional_protein)
        private val tvNutritionalFat: TextView = view.findViewById(R.id.tv_nutritional_fat)

        fun setup(feedItem: FeedItem) {
            val resources = itemView.context.resources
            tvNutritionalEnergy.text =
                resources.getString(R.string.qty_energy, AppUtil.formatKcal(feedItem.energy!!))
            tvNutritionalCarbohydrate.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.carbohydrate!!))
            tvNutritionalProtein.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.protein!!))
            tvNutritionalFat.text =
                resources.getString(R.string.weight, AppUtil.formatWeight(feedItem.fat!!))
        }
    }
}