package pl.michalmaslak.samplemobileapp.ui.main.workorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.layout_work_order_list_item.view.*
import pl.michalmaslak.samplemobileapp.R
import pl.michalmaslak.samplemobileapp.models.WorkOrder
import pl.michalmaslak.samplemobileapp.util.DateUtils
import pl.michalmaslak.samplemobileapp.util.GenericViewHolder

class WorkOrderListAdapter(
    private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG:String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val WORK_ORDER_ITEM = 0
    private val NO_MORE_RESULTS_WORK_ORDER_MARKER = WorkOrder(
        NO_MORE_RESULTS,
        "",
        "",
        "",
        "",
        0,
        0,
        ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkOrder>() {

        override fun areItemsTheSame(oldItem: WorkOrder, newItem: WorkOrder): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: WorkOrder, newItem: WorkOrder): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(
        WorkOrderRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType){

            NO_MORE_RESULTS -> {
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            WORK_ORDER_ITEM -> {
                return WorkOrderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_work_order_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction
                )
            }

            else -> {
                return WorkOrderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_work_order_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction
                )
            }
        }
    }

    internal inner class WorkOrderRecyclerChangeCallback(
        private val adapter: WorkOrderListAdapter
    ): ListUpdateCallback{
        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkOrderViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList.get(position).pk > -1){
            return WORK_ORDER_ITEM
        }
        return differ.currentList.get(position).pk
    }

    fun submitList(workOrderList: List<WorkOrder>?, isQueryExhausted: Boolean) {
        val newList = workOrderList?.toMutableList()
        if(isQueryExhausted){
            newList?.add(NO_MORE_RESULTS_WORK_ORDER_MARKER)
        }
        val commitCallback = Runnable {
            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class WorkOrderViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: WorkOrder) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.work_order_title.text = item.title
            itemView.work_order_status.text = item.status
            itemView.work_order_created_by.text = item.created_by
            itemView.work_order_created_at.text = DateUtils.convertLongToStringDate(item.created_at)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: WorkOrder)
        fun restoreListPosition()
    }


}

