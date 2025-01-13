package com.wavez.trackerwater.feature.insights.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R

import com.wavez.trackerwater.databinding.ItemDetailInsightBinding
import com.wavez.trackerwater.feature.insights.model.InsightsParts


class InsightDetailPagerAdapter(
    private val textColor: Int
) : BaseRecyclerViewAdapter<InsightsParts>() {


    companion object {
        private const val TYPE_ITEM = 1

    }

    inner class InsightDetailHolder(val binding: ItemDetailInsightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItemInsight(content: InsightsParts) {
            val context = binding.root.context
            binding.tvTitleItemDetailInsight.text =
                binding.root.context.getString(content.contentTitle)
            binding.tvItemDetailInsight.text =
                binding.root.context.getString(content.contentDescription)
            binding.tvTitleItemDetailInsight.setTextColor(
                ContextCompat.getColor(
                    context, textColor
                )
            )
            binding.tvItemDetailInsight.setTextColor(ContextCompat.getColor(context, textColor))
        }
    }


    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder, item: InsightsParts, position: Int
    ) {
        (holder as InsightDetailHolder).bindItemInsight(item)
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_detail_insight
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: InsightsParts): Int {
        return TYPE_ITEM
    }


    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemDetailInsightBinding.bind(view)
            return InsightDetailHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

}
