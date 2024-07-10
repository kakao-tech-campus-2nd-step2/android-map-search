package campus.tech.kakao.map.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Item
import campus.tech.kakao.map.repository.location.LocationSearcher

class SearchViewModel(private val locationSearcher: LocationSearcher) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun searchLocationData(keyword: String) {
        _items.value = locationSearcher.search(keyword)
    }
}