package campus.tech.kakao.map.Presenter.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Domain.Model.Place
import campus.tech.kakao.map.R

class FavoriteAdapter(
    private var favorites: List<Place>,
    private val inflater: LayoutInflater,
    val onClickDelete: (name: String) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View, onClickDelete: (name: String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        var placeName: TextView
        var deleteFavorite: ImageView

        init {
            placeName = itemView.findViewById<TextView>(R.id.favoriteName)
            deleteFavorite = itemView.findViewById<ImageView>(R.id.deleteFavorite)

            deleteFavorite.setOnClickListener {
                onClickDelete.invoke(placeName?.text.toString())
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorites[position]
        holder.placeName.text = favorite.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.favorite_element, parent, false)
        return ViewHolder(view, onClickDelete)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun updateData(favoriteList: List<Place>) {
        this.favorites = favoriteList
        notifyDataSetChanged()
    }


}



