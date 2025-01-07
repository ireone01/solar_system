
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lingvo.base_common.ui.BaseRecyclerViewAdapter
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : BaseRecyclerViewAdapter<DrinkModel>() {

    companion object {

        private const val TYPE_ITEM = 1

        private const val TYPE_ADS = 2

    }

    var onSelected: ((DrinkModel) -> Unit)? = null

    var onUpdate: ((DrinkModel) -> Unit)? = null

    var onDelete: ((DrinkModel) -> Unit)? = null


    override fun bindViewHolder(
        holder: RecyclerView.ViewHolder, item: DrinkModel, position: Int
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

    override fun getItemType(item: DrinkModel): Int {
        return TYPE_ITEM
    }

    inner class HistoryHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            drinkModel: DrinkModel
        ) {
            val amount = drinkModel.amountDrink * 0.001
            binding.tvAmount.text = String.format("%.3f l", amount)
            val date = Date(drinkModel.dateDrink)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            binding.tvTime.text = dateFormat.format(date)
            binding.ivEdit.setOnClickListener { onUpdate?.invoke(drinkModel) }
            binding.ivDelete.setOnClickListener { onDelete?.invoke(drinkModel) }
        }
    }

}