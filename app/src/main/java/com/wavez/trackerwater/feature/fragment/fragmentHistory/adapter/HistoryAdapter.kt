package com.wavez.trackerwater.feature.fragment.fragmentHistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ItemHistoryBinding
import com.wavez.trackerwater.feature.fragment.adapter.DrinkAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter (
    internal val historyList: MutableList<DrinkModel>,
    private val onDelete: (DrinkModel) -> Unit,
    private val onUpdate: (DrinkModel) -> Unit
): RecyclerView.Adapter<HistoryAdapter.HistoryHolder>(){

    class HistoryHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(
            drinkModel: DrinkModel,
            onDelete: (DrinkModel) -> Unit,
            onUpdate: (DrinkModel) -> Unit){
            val amount = drinkModel.amountDrink * 0.001
            binding.tvAmount.text = String.format("%.3f l", amount)
            val date = Date(drinkModel.dateDrink)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            binding.tvTime.text = dateFormat.format(date)
            binding.ivEdit.setOnClickListener { onUpdate(drinkModel) }
            binding.ivDelete.setOnClickListener { onDelete(drinkModel) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return HistoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(historyList[position], onDelete, onUpdate)
    }

    fun updateList(newItems: List<DrinkModel>) {
        historyList.clear()
        historyList.addAll(newItems)
        notifyDataSetChanged()
    }
}