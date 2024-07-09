package com.example.kakaotechcampus_2nd_step2_android.viewmodel.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kakaotechcampus_2nd_step2_android.model.Item
import com.example.kakaotechcampus_2nd_step2_android.repository.location.LocationSearcher

class SearchViewModel(private val locationSearcher: LocationSearcher) : ViewModel() {
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    fun searchLocationData(keyword: String) {
        _items.value = locationSearcher.search(keyword)
    }
}
