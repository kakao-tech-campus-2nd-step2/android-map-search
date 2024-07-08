package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.DTO.Document
import campus.tech.kakao.map.R

class DocumentAdapter(
	val onItemClicked: (Document) -> Unit
): ListAdapter<Document, DocumentAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<Document>(){
		override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
			return (oldItem.place_name == newItem.place_name)
					&& (oldItem.address_name == newItem.address_name)
					&& (oldItem.category_group_name == newItem.category_group_name)
		}

		override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
			return oldItem == newItem
		}

	}
) {
	private var placeClicked = { position:Int ->
		val document: Document = getItem(position)
		onItemClicked(document)
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
		val document: Document = getItem(position)
		holder.name.text = document.place_name
		holder.address.text = document.address_name
		holder.type.text = document.category_group_name
	}
}


