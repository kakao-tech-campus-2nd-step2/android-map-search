package campus.tech.kakao.map

import android.media.RouteListingPreference
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
class ItemAdapter(private val items: List<RouteListingPreference.Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    override fun getItemCount() = items.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeResult: TextView = itemView.findViewById(R.id.result)
        fun bind(item: ) {
            placeResult.text = item.

        }
    }
}