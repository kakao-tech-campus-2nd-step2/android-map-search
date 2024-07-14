package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.LocationCallBack
import campus.tech.kakao.map.model.SearchLocationRepository

class SearchLocationViewModel : ViewModel() {
    private lateinit var repository: SearchLocationRepository
    fun setRepository(repository: SearchLocationRepository) {
        this.repository = repository
        _history.value = repository.getHistory()
    }

    private val _location = MutableLiveData<List<Location>>()
    val location: LiveData<List<Location>> = _location

    private val _history = MutableLiveData<List<String>>()
    val history: LiveData<List<String>> = _history

    fun searchLocation(category: String) {
        repository.searchLocation(category, object : LocationCallBack {
            override fun onSuccess(data: List<Location>) {
                _location.postValue(data)
            }

            override fun onFailure(e: Exception) {
                Log.e("SearchLocationViewModel", "searchLocation failed (${e.message})", e)
            }
        })
    }

    fun addHistory(locationName: String) {
        repository.addHistory(locationName)
        _history.value = repository.getHistory()
    }

    fun removeHistory(locationName: String) {
        repository.removeHistory(locationName)
        _history.value = repository.getHistory()
    }
}