package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(
	val onItemClicked: (Place) -> Unit
): ListAdapter<Place, PlaceAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<Place>(){
		override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
			return (oldItem.name == newItem.name)
					&& (oldItem.address == newItem.address)
					&& (oldItem.type == newItem.type)
		}

		override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
			return oldItem == newItem
		}

	}
) {
	private var placeClicked = { position:Int ->
		val place:Place = getItem(position)
		onItemClicked(place)
	}
	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		val name:TextView = itemView.findViewById(R.id.name)
		val address:TextView = itemView.findViewById(R.id.address)
		val type:TextView = itemView.findViewById(R.id.type)
		init {
			itemView.setOnClickListener {
				placeClicked(bindingAdapterPosition)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val place:Place = getItem(position)
		holder.name.text = place.name
		holder.address.text = place.address
		holder.type.text = place.type
	}
}


