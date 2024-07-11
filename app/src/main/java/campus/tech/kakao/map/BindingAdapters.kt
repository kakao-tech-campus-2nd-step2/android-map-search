package campus.tech.kakao.map

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.model.PlaceData
import campus.tech.kakao.map.model.SearchResult

@BindingAdapter("placeList")
fun bindPlaceList(recyclerView: RecyclerView, data: List<PlaceData>?) {
    val adapter = recyclerView.adapter as? PlaceResultRecyclerViewAdapter
    adapter?.updateData(data ?: emptyList())
}

@BindingAdapter("searchHistoryList", "onClearButtonClicked", "onItemClicked")
fun bindSearchHistoryList(
    recyclerView: RecyclerView,
    data: List<SearchResult>?,
    onClearButtonClicked: (SearchResult) -> Unit,
    onItemClicked: (SearchResult) -> Unit
) {
    val adapter = recyclerView.adapter as? SearchHistoryRecyclerViewAdapter
    adapter?.let {
        it.updateData(data ?: emptyList())
        it.setOnClearButtonClickedListener(onClearButtonClicked)
        it.setOnItemClickedListener(onItemClicked)
    }
}
