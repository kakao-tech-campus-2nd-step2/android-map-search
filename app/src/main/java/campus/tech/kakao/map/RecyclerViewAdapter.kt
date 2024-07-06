package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    var placeList: MutableList<Place>,
    private val onItemClicked: (Place) -> Unit
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
                    onItemClicked(place)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.place_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = placeList.get(position)

        val img = place.category.imgId

        holder.img.setImageResource(img)
        holder.name.text = place.name
        holder.location.text = place.location
        holder.category.text = place.category.category
    }
}