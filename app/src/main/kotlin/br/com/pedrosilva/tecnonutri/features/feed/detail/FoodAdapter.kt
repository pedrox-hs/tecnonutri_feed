package br.com.pedrosilva.tecnonutri.features.feed.detail

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.ext.formatDecimal
import br.com.pedrosilva.tecnonutri.ext.formatKcal
import br.com.pedrosilva.tecnonutri.ext.formatWeight
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Food
import kotlinx.android.synthetic.main.item_food.view.tv_description
import kotlinx.android.synthetic.main.item_food.view.tv_qty
import kotlinx.android.synthetic.main.item_food_nutritional.view.tv_nutritional_carbohydrate
import kotlinx.android.synthetic.main.item_food_nutritional.view.tv_nutritional_energy
import kotlinx.android.synthetic.main.item_food_nutritional.view.tv_nutritional_fat
import kotlinx.android.synthetic.main.item_food_nutritional.view.tv_nutritional_protein

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

        fun setup(food: Food) = itemView.run {
            val resources = itemView.context.resources
            tv_description.text = food.description
            tv_qty.text = resources.getString(
                R.string.qty_food,
                food.amount?.formatDecimal().orEmpty(),
                food.measure,
                food.weight?.formatWeight().orEmpty()
            )
            tv_nutritional_energy.text = food.energy?.formatKcal(resources)
            tv_nutritional_carbohydrate.text = food.carbohydrate?.formatWeight(resources)
            tv_nutritional_protein.text = food.protein?.formatWeight(resources)
            tv_nutritional_fat.text = food.fat?.formatWeight(resources)
        }
    }

    private inner class ViewHolderTotal(view: View) : RecyclerView.ViewHolder(view) {

        fun setup(feedItem: FeedItem) = itemView.run {
            val resources = itemView.context.resources
            tv_nutritional_energy.text = feedItem.energy?.formatKcal(resources)
            tv_nutritional_carbohydrate.text = feedItem.carbohydrate?.formatWeight(resources)
            tv_nutritional_protein.text = feedItem.protein?.formatWeight(resources)
            tv_nutritional_fat.text = feedItem.fat?.formatWeight(resources)
        }
    }

    private fun Number.formatKcal(resources: Resources) =
        resources.getString(R.string.qty_energy, formatKcal().orEmpty())

    private fun Number.formatWeight(resources: Resources) =
        resources.getString(R.string.weight, formatWeight().orEmpty())
}