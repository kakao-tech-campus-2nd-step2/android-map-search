package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.SearchResult

class SearchHistoryRecyclerViewAdapter(
    private var searchHistoryList: MutableList<SearchResult>,
    private val searchEditText: EditText
) : RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.SearchHistoryViewHolder>() {

    private var onClearButtonClicked: ((SearchResult) -> Unit)? = null
    private var onItemClicked: ((SearchResult) -> Unit)? = null

    class SearchHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nthSearchHistoryKeyword: TextView = view.findViewById(R.id.nthSearchHistoryKeyword)
        val clearButton: ImageView = view.findViewById(R.id.itemClearButton)
    }

    fun updateData(newData: List<SearchResult>) {
        searchHistoryList.clear()
        searchHistoryList.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnClearButtonClickedListener(listener: (SearchResult) -> Unit) {
        onClearButtonClicked = listener
    }

    fun setOnItemClickedListener(listener: (SearchResult) -> Unit) {
        onItemClicked = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_history_item, parent, false)
        return SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = searchHistoryList.size

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item = searchHistoryList[position]
        holder.nthSearchHistoryKeyword.text = item.keyword

        holder.clearButton.setOnClickListener {
            onClearButtonClicked?.invoke(item)
        }
        holder.itemView.setOnClickListener {
            searchEditText.setText(item.keyword)
            onItemClicked?.invoke(item)
        }
    }
}
