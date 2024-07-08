package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(
    private val onItemClick: (Place) -> Unit,
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    private var places: List<Place> = emptyList()

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val addressTextView: TextView = itemView.findViewById(R.id.addressTextView)
        val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int,
    ) {
        val place = places[position]
        holder.nameTextView.text = place.name
        holder.addressTextView.text = place.address
        holder.typeTextView.text = place.type
        holder.itemView.setOnClickListener {
            onItemClick(place)
        }
    }

    fun updateData(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}
