package com.wavez.trackerwater.feature.insights.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ItemParentInsightBinding
import com.wavez.trackerwater.feature.insights.model.InsightsChildItem
import com.wavez.trackerwater.feature.insights.model.InsightsItem

class InsightAdapter(
    private val onChildItemClicked : (InsightsChildItem) -> Unit
) : BaseRecyclerViewAdapter<InsightsItem>(){

    companion object {
        private const val TYPE_ITEM = 1

    }

    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: InsightsItem,
        position: Int
    ) {
        if (holder is ParentInsightHolder) {
            holder.bind(item)
        } else {
            throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_parent_insight
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemParentInsightBinding.bind(view)
            return ParentInsightHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: InsightsItem): Int {
        return TYPE_ITEM
    }

    inner class ParentInsightHolder(private val binding: ItemParentInsightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: InsightsItem) {
            val context = binding.root.context
            binding.tvHeader.text =  context.getString(data.title)

            val adapter = InsightsChildAdapter{ insightsChildItem ->
                onChildItemClicked(insightsChildItem)
            }
            binding.rvChildInsight.adapter = adapter
            adapter.setData(data.insightsChild)

        }
    }


}

