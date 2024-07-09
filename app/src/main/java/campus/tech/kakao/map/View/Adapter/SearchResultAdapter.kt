package campus.tech.kakao.map.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.Model.PlaceCategory

class SearchResultAdapter(
    private var places: List<Place>,
    private val inflater: LayoutInflater,
    val onClickAdd: (name: String) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, onClickAdd: (name: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        var placeName: TextView
        var placeAddress: TextView
        var placeCategory: TextView

        init {
            placeName = itemView.findViewById<TextView>(R.id.placeName)
            placeAddress = itemView.findViewById<TextView>(R.id.placeAddress)
            placeCategory = itemView.findViewById<TextView>(R.id.placeCategory)

            itemView.setOnClickListener {
                onClickAdd.invoke(placeName.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.place_element, parent, false)

        return ViewHolder(view, onClickAdd)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address ?: ""
        holder.placeCategory.text =
            PlaceCategory.categoryToString(place.category ?: PlaceCategory.ELSE)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updateData(placeList: List<Place>) {
        places = placeList
        notifyDataSetChanged()
    }
}