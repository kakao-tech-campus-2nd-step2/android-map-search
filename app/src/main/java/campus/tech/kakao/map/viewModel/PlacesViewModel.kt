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

    private val _places: MutableLiveData<List<Place>> = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    init {
        _places.postValue(emptyList())
    }

    private fun loadDBPlaces() {
        viewModelScope.launch() {
            val placesFromRepo = repository.getAllLocalPlaces()
            _places.postValue(placesFromRepo)
        }
    }

    fun insertDBPlace(name: String, address: String, category: String = "") {
        viewModelScope.launch() {
            repository.insertLocalPlace(name, address, category)
            loadDBPlaces()
        }
    }

    fun deleteDBPlace(name: String, address: String, category: String = "") {
        viewModelScope.launch() {
            repository.deleteLocalPlace(name, address, category)
            loadDBPlaces()
        }
    }

    fun getAllDBPlaces(): List<Place> {
        return repository.getAllLocalPlaces()
    }

    fun filterDBPlaces(search: String) {
        viewModelScope.launch() {
            val allPlaces = repository.getAllLocalPlaces()
            val filtered = if (search.isEmpty()) {
                emptyList()
            } else {
                allPlaces.filter { it.name.contains(search, ignoreCase = true) }
            }
            _places.postValue(filtered)
        }
    }

    fun filterPlaces(search: String) {
        if (search == "") {
            _places.value = mutableListOf()
            return
        }
        RetrofitClient.retrofitService.requestPlaces(query = search).enqueue(object :
            Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val responseList = mutableListOf<Place>()
                    body?.documents?.forEach {
                        responseList.add(Place(it.placeName, it.addressName, it.categoryGroupName))
                    }
                    _places.value = responseList.toMutableList()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                println("error: $t")
            }
        })
    }
}