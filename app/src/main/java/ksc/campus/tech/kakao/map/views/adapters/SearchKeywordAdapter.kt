package ksc.campus.tech.kakao.map.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ksc.campus.tech.kakao.map.R


class SearchKeywordAdapter(
    private val inflater: LayoutInflater,
    private val clickCallback: SearchKeywordClickCallback
) : ListAdapter<String, SearchKeywordAdapter.SearchKeywordViewHolder>(object: DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = true
}) {

    class SearchKeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        val deleteText: View

        init {
            nameText = itemView.findViewById(R.id.text_search_keyword)
            deleteText = itemView.findViewById(R.id.view_delete_keyword)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeywordViewHolder {
        val view = inflater.inflate(R.layout.item_search_keyword, parent, false)
        val holder = SearchKeywordViewHolder(view)
        view.setOnClickListener {
            clickCallback.clickKeyword(holder.nameText.text.toString())
        }
        holder.deleteText.setOnClickListener {
            clickCallback.clickRemove(holder.nameText.text.toString())
        }
        return holder
    }

    override fun onBindViewHolder(holder: SearchKeywordViewHolder, position: Int) {
        val item = currentList[position]
        holder.nameText.text = item
    }
}