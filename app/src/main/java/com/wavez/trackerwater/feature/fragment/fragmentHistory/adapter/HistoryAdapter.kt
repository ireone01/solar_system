package com.wavez.trackerwater.feature.fragment.fragmentHistory.adapter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : BaseRecyclerViewAdapter<HistoryModel>() {

    companion object {

        private const val TYPE_ITEM = 1

        private const val TYPE_ADS = 2

    }

    var onSelected: ((HistoryModel) -> Unit)? = null

    var onUpdate: ((HistoryModel) -> Unit)? = null

    var onDelete: ((HistoryModel) -> Unit)? = null


    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder, item: HistoryModel, position: Int
    ) {
        if (holder is HistoryHolder) {
            holder.bind(item)
        } else {
            throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemLayout(viewType: Int): Int {
        if (viewType == TYPE_ITEM) {
            return R.layout.item_history
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun createViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = ItemHistoryBinding.bind(view)
            return HistoryHolder(binding)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemType(item: HistoryModel): Int {
        return TYPE_ITEM
    }

    inner class HistoryHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            historyModel: HistoryModel
        ) {
            val amount = historyModel.amountHistory * 0.001
            binding.tvAmount.text = String.format("%.3f l", amount)
            val date = Date(historyModel.dateHistory)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            binding.tvTime.text = dateFormat.format(date)
            binding.ivEdit.setOnClickListener { onUpdate?.invoke(historyModel) }
            binding.ivDelete.setOnClickListener { onDelete?.invoke(historyModel) }
        }
    }

}