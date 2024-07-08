package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(
	val onItemClicked: (SearchWord) -> Unit
): ListAdapter<SearchWord, WordAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<SearchWord>(){
		override fun areItemsTheSame(oldItem: SearchWord, newItem: SearchWord): Boolean {
			return (oldItem.name == newItem.name)
					&& (oldItem.address == newItem.address)
					&& (oldItem.type == newItem.type)
		}

		override fun areContentsTheSame(oldItem: SearchWord, newItem: SearchWord): Boolean {
			return oldItem == newItem
		}

	}
) {
	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		val searchWord: TextView = itemView.findViewById(R.id.search_word)
		val delete: ImageView = itemView.findViewById(R.id.x)
		init {
			delete.setOnClickListener {
				deletedWords(bindingAdapterPosition)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val word = getItem(position)
		holder.searchWord.text = word.name
	}

	private val deletedWords = { position:Int ->
		val word = getItem(position)
		onItemClicked(word)
	}
}