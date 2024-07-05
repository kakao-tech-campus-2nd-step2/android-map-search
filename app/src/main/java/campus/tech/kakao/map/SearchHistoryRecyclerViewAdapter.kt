package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryRecyclerViewAdapter(
    private var searchHistory: MutableList<Place>,
    private val onItemClick: (Int) -> Unit,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchHistoryBtn: TextView = itemView.findViewById(R.id.search_history_item)
        val searchHistoryDeleteBtn: ImageView = itemView.findViewById(R.id.search_history_delete_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.searchHistoryBtn.text = searchHistory[index].name
        holder.searchHistoryBtn.setOnClickListener { onItemClick(index) }
        holder.searchHistoryDeleteBtn.setOnClickListener {
            onItemDelete(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, searchHistory.size);
        }
    }
    override fun getItemCount(): Int { return searchHistory.size }
}
