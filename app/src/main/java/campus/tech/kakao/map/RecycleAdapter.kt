package campus.tech.kakao.map

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemRecyclerviewBinding

class RecycleAdapter( val onClick : (String) -> (Unit)) : RecyclerView.Adapter<RecycleAdapter.Holder>() {

    private var cursor: Cursor? = null

    inner class Holder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String, address: String, category: String) {
            binding.Name.text = name
            binding.Address.text = address
            binding.Category.text = category
            binding.root.setOnClickListener{
                onClick(name)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapter.Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: RecycleAdapter.Holder, position: Int) {
        cursor?.apply{
            moveToPosition(position)
            val name = getString(getColumnIndexOrThrow(PlaceEntry.COLUMN_NAME))
            val address = getString(getColumnIndexOrThrow(PlaceEntry.COLUMN_ADDRESS))
            val category = getString(getColumnIndexOrThrow(PlaceEntry.COLUMN_CATEGORY))
            holder.bind(name, address, category)
        }
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }


    fun SubmitCursor(Cursor : Cursor?){
        cursor = Cursor
        notifyDataSetChanged()

    }
}
