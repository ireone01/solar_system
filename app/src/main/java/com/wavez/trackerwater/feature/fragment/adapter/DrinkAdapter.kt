package com.wavez.trackerwater.feature.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ItemGlassWaterBinding

class DrinkAdapter(
    private val items: MutableList<DrinkModel>,
    private val onClick: (DrinkModel) -> Unit
) : RecyclerView.Adapter<DrinkAdapter.DrinkHolder>() {

    class DrinkHolder(private val binding: ItemGlassWaterBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(drink: DrinkModel, onDelete: (DrinkModel) -> Unit) {
            binding.itemText.text = "+"+drink.amountDrink.toString()+" ml"
            binding.root.setOnClickListener { onDelete(drink) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkHolder {
        val binding = ItemGlassWaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrinkHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DrinkHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    fun updateList(newItems: List<DrinkModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

}
