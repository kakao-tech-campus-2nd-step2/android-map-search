package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SearchRecyclerViewAdapter(
    var searchList: List<PlaceDataModel>,
    private var inflater: LayoutInflater,
    private var context: Context
): RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nthBtnClose: ImageButton
        val nthPlaceName: TextView
        init {
            nthBtnClose = itemView.findViewById(R.id.btnClose)
            nthPlaceName = itemView.findViewById(R.id.tvPlaceName)
            nthBtnClose.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val search = searchList.get(position)
                val searchDatabaseAccess = SearchDatabaseAccess(context)
                searchDatabaseAccess.deleteSearch(search.name)
                updateSearchHistory()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nthPlaceName.text = searchList.get(position).name
    }

    fun updateSearch(newPlaceList: List<PlaceDataModel>) {
        val diffCallback = SearchDiffCallback(searchList, newPlaceList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        searchList = newPlaceList
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSearchHistory() {
        val searchDatabaseAccess = SearchDatabaseAccess(context)
        val updatedSearchHistory = searchDatabaseAccess.getAllSearch()
        updateSearch(updatedSearchHistory)
    }
}