package com.wavez.trackerwater.feature.insights.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ItemChildInsightBinding
import com.wavez.trackerwater.feature.insights.model.InsightsChildItem

class InsightsChildAdapter(
    private val textColor : Int ,
    private val onChildClicked: (InsightsChildItem) -> Unit


) : BaseRecyclerViewAdapter<InsightsChildItem>() {

    companion object {
        private const val TYPE_ITEM = 1

    }

    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder, item: InsightsChildItem, position: Int
    ) {
        if (holder is InsightChildHolder) {
            holder.bind(item)
        } else {
            throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_child_insight
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemChildInsightBinding.bind(view)
            return InsightChildHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: InsightsChildItem): Int {
        return TYPE_ITEM
    }

    inner class InsightChildHolder(private val binding: ItemChildInsightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: InsightsChildItem) {
            val context = binding.root.context
            binding.root.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, data.colorSecondary))
            binding.tvTileChildInsight.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, data.colorPrimary))
            binding.ivInsight.setImageResource(data.image)
            binding.tvTileChildInsight.text = context.getString(data.title)

            binding.root.setOnClickListener {
                onChildClicked(data)
            }

            binding.tvTileChildInsight.setTextColor(ContextCompat.getColor(context, textColor))


        }
    }


}

