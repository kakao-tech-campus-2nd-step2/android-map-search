package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.PlaceData

class PlaceResultRecyclerViewAdapter(
    private var placeList: MutableList<PlaceData>
): RecyclerView.Adapter<PlaceResultRecyclerViewAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeDataName)
        val placeLocation: TextView = view.findViewById(R.id.placeDataLocation)
        val placeCategory: TextView = view.findViewById(R.id.placeDataCategory)
    }

    fun updateData(newData: List<PlaceData>) {
        placeList.clear()
        placeList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = placeList[position]
        holder.placeName.text = item.name
        holder.placeLocation.text = item.location
        holder.placeCategory.text = item.category
    }
}