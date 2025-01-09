package com.wavez.trackerwater.feature.drink.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.feature.fragment.adapter.HistoryAdapter
import com.wavez.trackerwater.feature.fragment.adapter.HistoryAdapter.Companion
import com.wavez.trackerwater.feature.fragment.adapter.HistoryAdapter.DrinkHolder

class IntakeAdapter : BaseRecyclerViewAdapter<IntakeModel>() {

    companion object {
        private const val TYPE_ITEM = 1
    }

    var onSelect: ((IntakeModel) -> Unit)? = null

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: IntakeModel, position: Int) {
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

    override fun getItemType(item: IntakeModel): Int {
        return TYPE_ITEM
    }

    inner class HistoryHolder(private val binding: ItemGlassWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(intake: IntakeModel) {
            binding.itemText.text = intake.amountIntake.toString()
            binding.root.setOnClickListener { onSelect?.invoke(intake) }
            binding.tvCount.gone()
        }
    }
}