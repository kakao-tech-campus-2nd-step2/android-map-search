package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSearchResultBinding

class StoreAdapter(private val onItemClicked: (StoreEntity) -> Unit) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private var searchResults = listOf<StoreEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.bind(searchResults[position], onItemClicked)
        Log.d("testt", "StoreonBindViewHolder $position")
    }

    override fun getItemCount(): Int = searchResults.size

    fun updateSearchResults(results: List<StoreEntity>) {
        searchResults = results
        notifyDataSetChanged()
    }

    class StoreViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(store: StoreEntity, onItemClicked: (StoreEntity) -> Unit) {
            binding.textViewName.text = store.name
            binding.textViewLocation.text = store.location
            binding.root.setOnClickListener {
                onItemClicked(store)
            }
        }
    }
}
