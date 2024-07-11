package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.Place

class SearchAdapter(
    private var places: List<Place>,
    private val onItemClickListener: (Place) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(
        itemView: View,
        private val onItemClickListener: (Place) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.placeName)
        private val addressTextView: TextView = itemView.findViewById(R.id.placeAddress)
        private val categoryTextView: TextView = itemView.findViewById(R.id.placeCategory)

        fun bind(place: Place) {
            nameTextView.text = place.name
            addressTextView.text = place.address
            categoryTextView.text = place.category
            itemView.setOnClickListener {
                onItemClickListener(place)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return SearchViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updateData(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }

}