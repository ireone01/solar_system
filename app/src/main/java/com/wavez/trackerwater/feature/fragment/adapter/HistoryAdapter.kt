package com.wavez.trackerwater.feature.fragment.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.HistoryModelWithCount
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding

class HistoryAdapter() : BaseRecyclerViewAdapter<HistoryModelWithCount>() {

    companion object {
        private const val TYPE_ITEM = 1
    }

    var onSelect: ((HistoryModelWithCount) -> Unit)? = null

    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder, item: HistoryModelWithCount, position: Int
    ) {
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

    override fun getItemType(item: HistoryModelWithCount): Int {
        return TYPE_ITEM
    }

    inner class DrinkHolder(private val binding: ItemGlassWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(drink: HistoryModelWithCount) {
            binding.itemText.text = "+" + drink.amountHistory.toString() + " ml"
            binding.tvCount.text = drink.count.toString()
            binding.root.setOnClickListener { onSelect?.invoke(drink) }
        }
    }
}