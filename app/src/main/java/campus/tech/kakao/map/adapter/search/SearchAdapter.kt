package com.example.kakaotechcampus_2nd_step2_android.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import com.example.kakaotechcampus_2nd_step2_android.model.Item
import com.example.kakaotechcampus_2nd_step2_android.viewmodel.OnSearchItemClickListener

class SearchAdapter(
    private val onSearchItemClickListener: OnSearchItemClickListener
) :
    ListAdapter<Item, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.place == newItem.place

            override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            val item = getItem(position)
            holder.bindViewHolder(item, onSearchItemClickListener)
        }
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemList: RelativeLayout = view.findViewById(R.id.item_list)
        private val place: TextView = view.findViewById(R.id.place_name)
        private val address: TextView = view.findViewById(R.id.address)
        private val category: TextView = view.findViewById(R.id.category)

        fun bindViewHolder(item: Item, onSearchItemClickListener: OnSearchItemClickListener) {
            place.text = item.place
            address.text = item.address
            category.text = item.category

            itemList.setOnClickListener {
                onSearchItemClickListener.onSearchItemClick(item)
            }
        }
    }
}
