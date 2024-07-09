package campus.tech.kakao.map.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get() = _places
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory


    fun searchPlaces(apiKey: String, query: String) {
        placeRepository.searchPlaces(apiKey, query) { result ->
            _places.postValue(result)
        }
    }

//    fun saveSearchQuery(query: String) {
//        repository.saveSearchHistory(query)
//        loadSearchHistory()
//    }
//
//    fun removeSearchQuery(query: String) {
//        repository.removeSearchQuery(query)
//        loadSearchHistory()
//    }
}
