package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var profiles: List<Profile>) : RecyclerView.Adapter<Adapter.ProfileViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(name: String)
    }

    var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvname)
        val tvAddress: TextView = itemView.findViewById(R.id.tvaddress)
        val tvType: TextView = itemView.findViewById(R.id.tvtype)

        init {
            itemView.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    listener?.onItemClick(profiles[position].name)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.tvName.text = profile.name
        holder.tvAddress.text = profile.address
        holder.tvType.text = profile.type
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    fun updateProfiles(newProfiles: List<Profile>) {
        profiles = newProfiles
        notifyDataSetChanged()
    }
}
