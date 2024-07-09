package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceViewModel(private val context: Context) : ViewModel() {
    private val repository: PlaceRepository = PlaceRepository(context)
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get() = _places
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory

    private fun loadPlaces() {
        val loadedPlaces = repository.getAllPlaces()
        _places.postValue(loadedPlaces)
    }

    fun loadSearchHistory() {
        val history = repository.getSearchHistory().toList()
        _searchHistory.postValue(history)
    }

    fun insertPlace(place: Place) {
        repository.insertPlace(place)
        loadPlaces()
    }

    fun getAllPlaces() {
        loadPlaces()
    }

    fun searchPlaces(query: String) {
        val searchedPlacees = repository.searchPlaces(query)
        _places.postValue(searchedPlacees)
    }

    fun saveSearchQuery(query: String) {
        repository.saveSearchHistory(query)
        loadSearchHistory()
    }

    fun removeSearchQuery(query: String) {
        repository.removeSearchQuery(query)
        loadSearchHistory()
    }
}
