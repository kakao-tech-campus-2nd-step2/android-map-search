package campus.tech.kakao.map.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.ui.search.SearchActivity

class ResultRecyclerViewAdapter(private val clickListener: SearchActivity.OnPlaceItemClickListener) :
    ListAdapter<Place, ResultRecyclerViewAdapter.PlaceViewHolder>(PlaceDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int,
    ) {
        val place = getItem(position)
        holder.bind(place)
        holder.itemView.setOnClickListener {
            clickListener.onPlaceItemClicked(place)
        }
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(place: Place) {
            itemView.findViewById<TextView>(R.id.place_name_text_view).text = place.name
            itemView.findViewById<TextView>(R.id.place_category_text_view).text = place.category
            itemView.findViewById<TextView>(R.id.place_address_text_view).text = place.address
        }
    }

    private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(
            oldItem: Place,
            newItem: Place,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Place,
            newItem: Place,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
