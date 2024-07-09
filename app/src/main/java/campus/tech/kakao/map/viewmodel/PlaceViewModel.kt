package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> get() = _searchResults

    fun searchPlacesByCategory(categoryGroupCode: String) {
        placeRepository.getPlacesByCategory(categoryGroupCode) { places ->
            viewModelScope.launch {
                _searchResults.emit(places)
            }
        }
    }
}
