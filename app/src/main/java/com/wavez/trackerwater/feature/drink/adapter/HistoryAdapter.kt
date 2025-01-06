package com.wavez.trackerwater.feature.drink.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding

class HistoryAdapter(
    private val drinkModels: MutableList<DrinkModel>,
    private val onSelect: (DrinkModel) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {
    class HistoryHolder(private val binding: ItemGlassWaterBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: DrinkModel, onSelect: (DrinkModel) -> Unit) {
            binding.itemText.text = history.amountDrink.toString()
            binding.root.setOnClickListener { onSelect(history) }
            binding.tvCount.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val binding = ItemGlassWaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryHolder(binding)
    }

    override fun getItemCount(): Int = drinkModels.size

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(drinkModels[position], onSelect)
    }

    fun updateData(newData: List<DrinkModel>) {
        drinkModels.clear()
        drinkModels.addAll(newData)
        notifyDataSetChanged() // Thông báo RecyclerView rằng dữ liệu đã thay đổi
    }
}