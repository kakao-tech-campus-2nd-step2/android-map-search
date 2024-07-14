package campus.tech.kakao.map.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.DiffUtilCallback
import campus.tech.kakao.map.databinding.ListItemBinding
import campus.tech.kakao.map.model.Location


class LocationAdapter(
    private var onItemClicked: (Location) -> Unit
): ListAdapter<Location,LocationAdapter.LocationViewHolder>(DiffUtilCallback()) {

    inner class LocationViewHolder(private val binding: ListItemBinding )
        :RecyclerView.ViewHolder(binding.root){
            fun bind(location: Location){
                binding.location = location
                binding.root.setOnClickListener {
                    onItemClicked(location)
                }
            }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }
}