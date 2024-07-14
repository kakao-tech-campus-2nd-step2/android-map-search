package campus.tech.kakao.map

import androidx.recyclerview.widget.DiffUtil
import campus.tech.kakao.map.model.Location

class DiffUtilCallback: DiffUtil.ItemCallback<Location>(){
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}