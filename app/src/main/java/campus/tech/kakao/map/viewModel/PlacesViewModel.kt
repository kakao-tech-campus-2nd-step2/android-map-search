package campus.tech.kakao.map.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.dto.SearchResponse
import campus.tech.kakao.map.model.Place
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesViewModel(private val repository: MapRepository) : ViewModel() {

    val places: LiveData<List<Place>> = repository.places

    fun searchPlaces(search: String) {
        repository.searchPlaces(search)
    }

    fun searchDBPlaces(search: String) {
        repository.searchDBPlaces(search)
    }
}