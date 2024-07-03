package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class PlaceRecyclerViewAdapter(
    var placeList: List<PlaceDataModel>,
    var inflater: LayoutInflater
): RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nthPlaceName: TextView
        val nthPlaceAddress: TextView
        val nthPlaceCategory: TextView
        init {
            nthPlaceName = itemView.findViewById(R.id.tvPlaceName)
            nthPlaceAddress = itemView.findViewById(R.id.tvPlaceAddress)
            nthPlaceCategory = itemView.findViewById(R.id.tvPlaceCategory)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.place_item, parent, false)
        return PlaceRecyclerViewAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nthPlaceName.text = placeList.get(position).name
        holder.nthPlaceAddress.text = placeList.get(position).address
        holder.nthPlaceCategory.text = placeList.get(position).category
    }

    fun updatePlace(newPlaceList: List<PlaceDataModel>) {
        val diffCallback = PlaceDiffCallback(placeList, newPlaceList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        placeList = newPlaceList
        diffResult.dispatchUpdatesTo(this)
    }
}