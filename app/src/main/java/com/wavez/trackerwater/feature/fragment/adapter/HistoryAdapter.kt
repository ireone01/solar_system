package com.wavez.trackerwater.feature.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding

class HistoryAdapter(
    private val items: MutableList<HistoryModel>,
    private val onSelect: (HistoryModel) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {
    class HistoryHolder(private val binding: ItemGlassWaterBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: HistoryModel, onSelect: (HistoryModel) -> Unit) {
            binding.itemText.text = history.amountHistory.toString()
            binding.root.setOnClickListener { onSelect(history) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val binding = ItemGlassWaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(items[position], onSelect)
    }
}