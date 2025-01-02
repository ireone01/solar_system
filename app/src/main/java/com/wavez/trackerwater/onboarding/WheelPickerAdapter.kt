package com.wavez.trackerwater.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.databinding.WheelPickerItemBinding

class WheelPickerAdapter(private val ListPicker : List<Int> ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class PickerViewHolder(val binding : WheelPickerItemBinding )
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Int){
            binding.textWheel.text = item.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = WheelPickerItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return PickerViewHolder(binding)
    }

    override fun getItemCount(): Int = ListPicker.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PickerViewHolder).bind(ListPicker[position])
    }
}