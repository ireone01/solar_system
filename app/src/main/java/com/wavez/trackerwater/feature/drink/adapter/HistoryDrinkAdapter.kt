package com.wavez.trackerwater.feature.drink.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding
import com.wavez.trackerwater.extension.gone

class HistoryDrinkAdapter : BaseRecyclerViewAdapter<HistoryDrink>() {

    companion object {
        private const val TYPE_ITEM = 1
    }

    var onSelect: ((HistoryDrink) -> Unit)? = null

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: HistoryDrink, position: Int) {
        if (holder is HistoryHolder) {
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
            return HistoryHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: HistoryDrink): Int {
        return TYPE_ITEM
    }

    inner class HistoryHolder(private val binding: ItemGlassWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: HistoryDrink) {
            binding.itemText.text = history.amountHistory.toString()
            binding.root.setOnClickListener { onSelect?.invoke(history) }
            binding.tvCount.gone()
        }
    }
}