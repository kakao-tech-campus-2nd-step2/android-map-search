package campus.tech.kakao.map.viewmodel

import android.app.Application
import android.util.Log
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
    val searchKeyword = MutableStateFlow("")
    var currentSearchResult: SearchResult? = null

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
            Log.d("SearchViewModel", "All search results loaded: $results")
        }
    }

    fun addSearchResult(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOrUpdateSearchResult(keyword)
            getAllSearchResults()  // 최신 데이터를 가져오기 위해 호출
            Log.d("SearchViewModel", "Search result added/updated: $keyword")
        }
    }

    fun deleteSearchResult(searchResult: SearchResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSearchResult(searchResult.id)
            getAllSearchResults()
            Log.d("SearchViewModel", "Search result deleted: ${searchResult.keyword}")
        }
    }

    fun getAllPlaces() {
        viewModelScope.launch {
            val placesList = withContext(Dispatchers.IO) {
                repository.getAllPlaces()
            }
            _places.value = placesList
            Log.d("SearchViewModel", "All places loaded: $placesList")
        }
    }

    fun addPlace(name: String, location: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlace(name, location, category)
            getAllPlaces()  // 최신 데이터를 가져오기 위해 호출
            Log.d("SearchViewModel", "Place added: $name, $location, $category")
        }
    }

    fun searchPlaces(keyword: String) {
        viewModelScope.launch {
            val results = repository.searchPlaces(keyword)
            _places.value = results
            Log.d("SearchViewModel", "Places searched with keyword: $keyword, results: $results")
        }
    }

    fun onClearButtonClicked() {
        searchKeyword.value = ""
        Log.d("SearchViewModel", "Clear button clicked")
    }

    fun onClearButtonClickedFromView(searchResult: SearchResult) {
        deleteSearchResult(searchResult)
    }

    fun onItemClickedFromView(searchResult: SearchResult) {
        handleSearch(searchResult)
    }

    fun handleSearch(searchResult: SearchResult) {
        val keyword = searchResult.keyword
        if (keyword.isNotEmpty()) {
            addSearchResult(keyword)
            searchPlaces(keyword)
        }
    }
}
