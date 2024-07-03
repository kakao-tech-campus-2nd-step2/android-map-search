package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout.TabView

class TapViewAdapter(
    var researchList: MutableList<Place>,
    var inflater: LayoutInflater
) : RecyclerView.Adapter<TapViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cancelButton: ImageView
        val placeName: TextView
        init {
            cancelButton = itemView.findViewById(R.id.tab_close_button)
            placeName = itemView.findViewById(R.id.tab_place_textview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.tab_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val research: Place = researchList[position]
        holder.placeName.text = research.name
        // holder.cancelButton.setOnClickListner{}
    }

    override fun getItemCount(): Int = researchList.size
}