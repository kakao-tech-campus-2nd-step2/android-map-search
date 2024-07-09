package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceViewModel : ViewModel() {
    private val placeRepository = PlaceRepository()

    private val _searchResults = MutableLiveData<List<Place>>()
    val searchResults: LiveData<List<Place>> get() = _searchResults

    init {
        _searchResults.value = emptyList()
    }

    fun searchPlacesByCategory(categoryGroupCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.getPlacesByCategory(categoryGroupCode) { places ->
                _searchResults.postValue(places)
            }
        }
    }
}
