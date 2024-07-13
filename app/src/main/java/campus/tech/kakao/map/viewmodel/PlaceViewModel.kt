package campus.tech.kakao.map.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.DBHelper
import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.model.PlaceResponse

class PlaceViewModel(private val dbHelper: DBHelper) : ViewModel() {

    private val _places = MutableLiveData<List<Document>>()
    private val _savedQueries = MutableLiveData<MutableList<String>>()
    val places: LiveData<List<Document>> get() = _places
    val savedQueries: LiveData<MutableList<String>> get() = _savedQueries


    fun loadPlaces(query: String, categoryGroupCode: String) {
        dbHelper.searchPlaces(query, categoryGroupCode) { newPlaces ->
            _places.value = newPlaces
        }
    }

    fun addSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList()
        updatedList.add(query)
        _savedQueries.value = updatedList
    }

    fun removeSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList()
        updatedList.remove(query)
        _savedQueries.value = updatedList
    }
}