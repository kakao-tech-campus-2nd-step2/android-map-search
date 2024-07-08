package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SavedSearchWordItemBinding

class SavedSearchWordsAdapter (
    private val savedSearchWords: List<SearchWord>,
    private val layoutInflater: LayoutInflater,
    private val delSavedSearchWord: (SearchWord) -> Unit
) :
    RecyclerView.Adapter<SavedSearchWordsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: SavedSearchWordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val savedSearchWord: TextView = binding.SavedSearchWord
        init{
            binding.delSavedSearchWord.setOnClickListener {
                val searchWord = SearchWord(savedSearchWord.text.toString())
                delSavedSearchWord(searchWord)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SavedSearchWordItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedSearchWords.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = savedSearchWords[position]
        holder.savedSearchWord.text = item.searchword
    }
}