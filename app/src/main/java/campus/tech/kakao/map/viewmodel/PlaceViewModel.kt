package campus.tech.kakao.map.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.DBHelper
import campus.tech.kakao.map.model.Place

class PlaceViewModel(context: Context) : ViewModel() {

    private val _places = MutableLiveData<List<Place>>()
    private val _savedQueries = MutableLiveData<MutableList<String>>()
    private val dbHelper = DBHelper(context)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SavedQueries", Context.MODE_PRIVATE)

    val places: LiveData<List<Place>> get() = _places
    val savedQueries: LiveData<MutableList<String>> get() = _savedQueries


    init {
        _savedQueries.value = loadSavedQueries()
    }

    fun loadPlaces(query: String) {
        _places.value = dbHelper.searchPlaces(query)
    }

    fun addSavedQuery(query: String) {
        _savedQueries.value?.let {
            if (!it.contains(query)) {
                it.add(query)
                _savedQueries.value = it
                saveQueries(it)
            }
        }
    }

    fun removeSavedQuery(query: String) {
        _savedQueries.value?.let {
            it.remove(query)
            _savedQueries.value = it
            saveQueries(it)
        }
    }

    private fun loadSavedQueries(): MutableList<String> {
        val savedQueriesString = sharedPreferences.getString("queries", null)
        return if (!savedQueriesString.isNullOrEmpty()) {
            savedQueriesString.split(",").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveQueries(queries: MutableList<String>) {
        val editor = sharedPreferences.edit()
        editor.putString("queries", queries.joinToString(","))
        editor.apply()
    }
}