package campus.tech.kakao.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel(private val locationDbAccessor: LocationDbAccessor) : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()

    private val _searchedLocations = MutableLiveData<List<Location>>()
    val searchedLocations: LiveData<List<Location>> get() = _searchedLocations

    fun setLocations() {
        _locations.value = readLocationData()
        _searchedLocations.value = emptyList()
    }

    fun searchLocations(query: String): Int {
        val results = locationDbAccessor.searchLocations(query)
        _searchedLocations.value = if(results.isNotEmpty()) results else emptyList()
        return results.size
    }

    fun insertLocation() {
        for (i in 1..9) {
            locationDbAccessor.insertLocation("카페$i", "부산 부산진구 전포대로$i", "카페")
        }
        for (i in 1..9) {
            locationDbAccessor.insertLocation("음식점$i", "부산 부산진구 중앙대로$i", "음식점")
        }
    }
    private fun readLocationData(): MutableList<Location> {
        val result: MutableList<Location> = locationDbAccessor.getLocationAll()
        Log.d("jieun", "$result")
        return result
    }
}