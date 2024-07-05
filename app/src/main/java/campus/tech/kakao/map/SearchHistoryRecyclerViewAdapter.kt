package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryRecyclerViewAdapter(
    private var searchHistories: MutableList<Place>,
    private val onItemClick: (Int) -> Unit,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keywordBtn: TextView = itemView.findViewById(R.id.keyword_item)
        val deleteBtn: ImageView = itemView.findViewById(R.id.delete_button)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        holder.keywordBtn.text = searchHistories[index].name
        holder.keywordBtn.setOnClickListener { onItemClick(index) }
        holder.deleteBtn.setOnClickListener {
            onItemDelete(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, searchHistories.size);
            Log.d("index", "index: $index")
        }
    }
    override fun getItemCount(): Int { return searchHistories.size }
}
