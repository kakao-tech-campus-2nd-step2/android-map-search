package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedSearchAdapter(private var items : List<SavedSearch> , private val onCloseClick : (SavedSearch) -> Unit) : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val onCloseClick: (SavedSearch) -> Unit) : RecyclerView.ViewHolder(view){

        val close : ImageView = view.findViewById(R.id.close)
        val id : TextView = view.findViewById(R.id.saved_search_id)
        val name: TextView = view.findViewById(R.id.saved_search_name)

        fun bind(item: SavedSearch) {
            itemView.setOnClickListener {
                onCloseClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_saved_search, parent, false)
        return SavedSearchAdapter.ViewHolder(view, onCloseClick)
    }

    override fun onBindViewHolder(holder: SavedSearchAdapter.ViewHolder, position: Int) {
        val item = items[position]
        with(holder) {
            id.text = item.id.toString()
            name.text = item.name
            bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<SavedSearch>) {
        items = newItems
        notifyDataSetChanged()
    }

}

