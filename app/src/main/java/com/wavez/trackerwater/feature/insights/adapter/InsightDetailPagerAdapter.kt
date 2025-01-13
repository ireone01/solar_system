package com.wavez.trackerwater.feature.insights.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wavez.trackerwater.databinding.ItemDetailInsightBinding

class InsightDetailPagerAdapter(val contents: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    inner class InsightDetailHolder(val binding: ItemDetailInsightBinding): RecyclerView.ViewHolder(binding.root){
        fun bindItemInsight(content : Int){
            binding.tvItemDetailInsight.text = binding.root.context.getString(content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val binding= ItemDetailInsightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InsightDetailHolder(binding)
    }

    override fun getItemCount(): Int {
      return contents.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InsightDetailHolder).bindItemInsight(contents[position])
    }
}