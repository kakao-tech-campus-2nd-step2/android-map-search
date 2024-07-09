package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedSearchBinding


class SavedSearchAdapter(private val onItemClicked: (StoreEntity) -> Unit) :
    RecyclerView.Adapter<SavedSearchAdapter.SavedSearchViewHolder>() {

    private var savedSearches = listOf<StoreEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchViewHolder {
        val binding =
            ItemSavedSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSearchViewHolder, position: Int) {
        holder.bind(savedSearches[position], onItemClicked)
        Log.d("testt", "SavedonBindViewHolder $position")
    }

    override fun getItemCount(): Int = savedSearches.size

    fun updateSavedSearches(searches: List<StoreEntity>) {
        savedSearches = searches.reversed()
        notifyDataSetChanged()
    }

    class SavedSearchViewHolder(private val binding: ItemSavedSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: StoreEntity, onItemClicked: (StoreEntity) -> Unit) {
            binding.savedSearchName.text = store.name
            binding.root.setOnClickListener {
                onItemClicked(store)
            }
        }
    }
}
