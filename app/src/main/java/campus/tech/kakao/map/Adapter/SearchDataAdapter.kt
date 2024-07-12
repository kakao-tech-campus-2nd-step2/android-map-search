package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.ViewModel.RecentViewModel
import campus.tech.kakao.map.retrofit.Document

class SearchDataAdapter(
    private var items: List<Document>,
    private val recentViewModel: RecentViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SearchDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.search_data_name)
        val address: TextView = view.findViewById(R.id.search_data_address)
        val category: TextView = view.findViewById(R.id.search_data_category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_data_item, parent, false)
        return SearchDataViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val viewHolder = holder as SearchDataViewHolder
        viewHolder.name.text = item.name
        viewHolder.address.text = item.address
        if (item.categoryCode.isNullOrEmpty()) {
            viewHolder.category.text = item.categoryTail
        } else {
            viewHolder.category.text = item.categoryDescription
        }

        holder.itemView.setOnClickListener {
            val searchTime = System.currentTimeMillis()
            recentViewModel.addRecentData(item.name,item.address, searchTime)
        }
    }

    fun updateData(newItems: List<Document>) {
        items = newItems
        notifyDataSetChanged()
    }
}