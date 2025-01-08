package com.wavez.trackerwater.feature.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding
import com.wavez.trackerwater.feature.drink.adapter.HistoryDrinkAdapter

class DrinkAdapter : BaseRecyclerViewAdapter<DrinkModel>() {

    companion object {
        private const val TYPE_ITEM = 1
    }

    var onSelect: ((DrinkModel) -> Unit)? = null

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: DrinkModel, position: Int) {
        if (holder is DrinkHolder) {
            holder.bind(item)
        } else {
            throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_glass_water
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemGlassWaterBinding.bind(view)
            return DrinkHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: DrinkModel): Int {
        return TYPE_ITEM
    }

    inner class DrinkHolder(private val binding: ItemGlassWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(drink: DrinkModel) {
            binding.itemText.text = "+" + drink.amountDrink.toString() + " ml"
            binding.root.setOnClickListener { onSelect?.invoke(drink) }
        }
    }
}