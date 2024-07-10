package campus.tech.kakao.map.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class SavedPlaceAdapter(
    private val items: List<String>,
    private val onItemRemove: (String) -> Unit
) : RecyclerView.Adapter<SavedPlaceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val queryTextView: TextView = view.findViewById(R.id.queryText)
        val removeButton: ImageButton = view.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place_saved, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]
        holder.queryTextView.text = place
        holder.removeButton.setOnClickListener { onItemRemove(place) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
