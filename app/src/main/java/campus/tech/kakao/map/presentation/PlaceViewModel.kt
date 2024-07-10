package campus.tech.kakao.map.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get() = _places
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory
    val apiKey = BuildConfig.KAKAO_REST_API_KEY


    fun searchPlaces(query: String) {
        Log.d("testt", "Searching places for query: $apiKey")

        placeRepository.searchPlaces(apiKey, query) { result ->
            Log.d("testt", "Search result: $result")
            _places.postValue(result)
        }
    }

    fun saveSearchQuery(placeName: String) {
        //        placeRepository.saveSearchHistory(placeName)
//        loadSearchHistory()
    }

    fun removeSearchQuery(query: String) {
//        repository.removeSearchQuery(query)
//        loadSearchHistory()
    }
}
