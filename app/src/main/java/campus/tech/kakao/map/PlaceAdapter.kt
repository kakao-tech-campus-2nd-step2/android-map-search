package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(var items: List<Place>, val inflater: LayoutInflater, var itemClickListener: OnItemClickListener): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceAdapter.PlaceViewHolder {
        val view = inflater.inflate(R.layout.place_item, parent, false)
        return  PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceAdapter.PlaceViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.address.text = items[position].address
        holder.category.text = items[position].category
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(searchResults: List<Place>) {
        items = searchResults
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Place {
        return items[position]
    }

    inner class PlaceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val address: TextView
        val category: TextView

        init {
            name = itemView.findViewById<TextView>(R.id.place)
            address = itemView.findViewById<TextView>(R.id.address)
            category = itemView.findViewById<TextView>(R.id.category)

            itemView.setOnClickListener {
                itemClickListener.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}