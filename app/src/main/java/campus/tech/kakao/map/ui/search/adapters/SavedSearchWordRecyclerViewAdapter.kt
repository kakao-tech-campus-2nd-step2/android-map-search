package campus.tech.kakao.map.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.SavedSearchWord
import campus.tech.kakao.map.ui.search.SearchActivity

class SavedSearchWordRecyclerViewAdapter(private val clickListener: SearchActivity.OnSavedSearchWordClearImageViewClickListener) :
    ListAdapter<SavedSearchWord, SavedSearchWordRecyclerViewAdapter.SavedSearchWordViewHolder>(SavedSearchWordDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SavedSearchWordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_search_word, parent, false)
        return SavedSearchWordViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: SavedSearchWordViewHolder,
        position: Int,
    ) {
        val savedSearchWord = getItem(position)
        holder.bind(savedSearchWord)
        holder.itemView.findViewById<ImageView>(R.id.saved_search_word_clear_image_view).setOnClickListener {
            clickListener.onSavedSearchWordClearImageViewClicked(savedSearchWord)
        }
    }

    class SavedSearchWordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(savedSearchWord: SavedSearchWord) {
            itemView.findViewById<TextView>(R.id.saved_search_word_text_view).text = savedSearchWord.name
        }
    }

    private class SavedSearchWordDiffCallback : DiffUtil.ItemCallback<SavedSearchWord>() {
        override fun areItemsTheSame(
            oldItem: SavedSearchWord,
            newItem: SavedSearchWord,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SavedSearchWord,
            newItem: SavedSearchWord,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
