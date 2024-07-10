package campus.tech.kakao.map.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.R

class PlaceAdapter(
    private val items: List<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.placeName)
        val locationTextView: TextView = view.findViewById(R.id.placeLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]
        holder.nameTextView.text = place.name
        holder.locationTextView.text = place.location
        holder.itemView.setOnClickListener { onItemClick(place) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}