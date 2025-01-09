package com.m17_stepcounter.feature.setting.language

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ItemLanguageBinding
import com.wavez.trackerwater.feature.language.model.Language

class LanguageAdapter : BaseRecyclerViewAdapter<Language>() {

    companion object {

        private const val TYPE_ITEM = 1

    }

    var selectedLanguageCode: String? = null

    var onSelected: ((Language) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun onLanguageSelected(code: String) {
        selectedLanguageCode = code
        notifyDataSetChanged()
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Language, position: Int) {
        if (holder is LanguageViewHolder) {
            holder.bind(item)
        } else {
            throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_language
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemLanguageBinding.bind(view)
            return LanguageViewHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: Language): Int {
        return TYPE_ITEM
    }

    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Language) {
            binding.ivLanguageFlag.setImageResource(data.flagName)
            binding.tvLanguageName.text = data.name
            binding.root.isActivated = data.code == selectedLanguageCode
            binding.root.setOnClickListener {
                selectedLanguageCode = data.code
                onSelected?.invoke(data)
            }
        }
    }

}