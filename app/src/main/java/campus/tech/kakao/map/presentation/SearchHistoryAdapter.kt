package campus.tech.kakao.map.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class SearchHistoryAdapter(
    private var historyList: List<String>,
    private val onDeleteClick: (String) -> Unit,
    private val onItemClick: (String) -> Unit,
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyText: TextView = itemView.findViewById(R.id.searchHistoryText)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteHistoryButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int,
    ) {
        val item = historyList[position]
        holder.historyText.text = item
        holder.historyText.setOnClickListener {
            onItemClick(item)
        }
        holder.deleteButton.setOnClickListener {
            onDeleteClick(item)
        }
    }

    fun updateData(newHistoryList: List<String>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }
}
