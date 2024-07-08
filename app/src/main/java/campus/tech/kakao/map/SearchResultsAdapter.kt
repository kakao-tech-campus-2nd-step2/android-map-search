package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchResultItemBinding


class SearchResultsAdapter(
    private val searchResults: List<StoreInfo>,
    private val layoutInflater: LayoutInflater,
    private val saveStoreName: (SearchWord) -> Unit
) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: SearchResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val storeName: TextView = binding.storeName
        val storeLocation: TextView = binding.storeLocation
        val storeCategory: TextView = binding.storeCategory
        init{
            binding.root.setOnClickListener{
                val searchWord = SearchWord(storeName.text.toString())
                saveStoreName(searchWord)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchResultItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searchResults[position]
        holder.storeName.text = item.name
        holder.storeLocation.text = item.location
        holder.storeCategory.text = item.category
    }
}