package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DbHelper(application)

    private val _searchResults = MutableLiveData<List<MapItem>>()
    val searchResults: LiveData<List<MapItem>> get() = _searchResults

    fun insertData() {
        dbHelper.insertData()
    }

    fun searchPlaces(keyword: String) {
        val results = dbHelper.searchPlaces(keyword)
        _searchResults.postValue(results)
    }
}

data class MapItem(
    val name: String,
    val address: String,
    val category: String
)
