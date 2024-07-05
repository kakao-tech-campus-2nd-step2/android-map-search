package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    var placeList: MutableList<Place>,
    var inflater: LayoutInflater,
    var placeRepository: PlaceRepository
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView
        val name: TextView
        val location: TextView
        val category: TextView
        init {
            img = itemView.findViewById(R.id.place_img)
            name = itemView.findViewById(R.id.place_name)
            location = itemView.findViewById(R.id.place_location)
            category = itemView.findViewById(R.id.place_category)

            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val place = placeList[position]
                    placeRepository.insertLog(place)
                    (itemView.context as MainActivity).addResearchList(place)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.place_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = placeList.get(position)

        val img = when (place.category) {
            "카페" -> R.drawable.cafe
            "약국" -> R.drawable.hospital
            else -> R.drawable.location
        }

        holder.img.setImageResource(img)
        holder.name.text = place.name
        holder.location.text = place.location
        holder.category.text = place.category
    }

    fun updatePlaceList(newPlaceList: List<Place>) {
        val oldPlaceList = placeList.toMutableList()

        // Remove incorrect-filter
        for (i in placeList.size - 1 downTo 0) {
            if (!newPlaceList.contains(placeList[i])) {
                placeList.removeAt(i)
                notifyItemRemoved(i)
            }
        }

        // Add correct-filter
        for (i in newPlaceList.indices) {
            if (i >= placeList.size) {
                placeList.add(newPlaceList[i])
                notifyItemInserted(i)
            } else if (newPlaceList[i] != placeList[i]) {
                placeList[i] = newPlaceList[i]
                notifyItemChanged(i)
            }
        }
    }
}