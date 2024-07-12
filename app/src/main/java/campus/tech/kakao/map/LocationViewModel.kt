package campus.tech.kakao.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel(
    private val locationLocalRepository: LocationLocalRepository,
    private val locationRemoteRepository: LocationRemoteRepository
) : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()

    private val _searchedLocations = MutableLiveData<List<Location>>()
    val searchedLocations: LiveData<List<Location>> get() = _searchedLocations

    fun setLocationsFromDB() {
        _locations.value = readLocationFromDB()
        _searchedLocations.value = emptyList()
    }

    fun getSearchedLocationsSize(): Int {
        return _searchedLocations.value?.size ?: 0
    }

    fun setLocationsFromKakaoAPI() {
        _locations.value = emptyList()
        _searchedLocations.value = emptyList()
    }


    fun searchLocationsFromDB(query: String): Int {
        val results = locationLocalRepository.searchLocations(query)
        _searchedLocations.value = if (results.isNotEmpty()) results else emptyList()
        return results.size
    }

    fun insertLocationFromDB() {
        for (i in 1..9) {
            locationLocalRepository.insertLocation("카페$i", "부산 부산진구 전포대로$i", "카페")
        }
        for (i in 1..9) {
            locationLocalRepository.insertLocation("음식점$i", "부산 부산진구 중앙대로$i", "음식점")
        }
    }

    private fun readLocationFromDB(): MutableList<Location> {
        val result: MutableList<Location> = locationLocalRepository.getLocationAll()
        Log.d("jieun", "$result")
        return result
    }

    fun searchLocationsFromKakaoAPI(restApiKey: String, query: String, callback: (Int) -> Unit) {
        viewModelScope.launch {
            val results: List<Location> = locationRemoteRepository.getSearchKeyword(restApiKey, query)
            _searchedLocations.value = results
            Log.d("jieun", "results: "+results)
            callback(results.size)
        }
    }
}