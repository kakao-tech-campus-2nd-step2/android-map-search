package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.PlaceData
import campus.tech.kakao.map.model.SearchResult
import campus.tech.kakao.map.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SearchRepository = SearchRepository(application)
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> get() = _searchResults

    private val _places = MutableStateFlow<List<PlaceData>>(emptyList())
    val places: StateFlow<List<PlaceData>> get() = _places

    fun getAllSearchResults() {
        viewModelScope.launch {
            val results = withContext(Dispatchers.IO) {
                repository.getAllSearchResults()
            }
            _searchResults.value = results
        }
    }

    fun addSearchResult(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrUpdateSearchResult(keyword)
            getAllSearchResults()  // 최신 데이터를 가져오기 위해 호출
        }
    }

    fun deleteSearchResult(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSearchResult(id)
            getAllSearchResults()
        }
    }

    fun getAllPlaces() {
        viewModelScope.launch {
            val placesList = withContext(Dispatchers.IO) {
                repository.getAllPlaces()
            }
            _places.value = placesList
        }
    }

    fun addPlace(name: String, location: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlace(name, location, category)
            getAllPlaces()  // 최신 데이터를 가져오기 위해 호출
        }
    }

    fun searchPlaces(keyword: String) {
     viewModelScope.launch {
         val results = repository.searchPlaces(keyword)
         _places.value = results
     }
    }

}
