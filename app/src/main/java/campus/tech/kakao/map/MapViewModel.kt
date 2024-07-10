package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MapViewModel(mContext: Context, val databaseListener: DatabaseListener) : ViewModel() {
    private val model = MapModel(mContext)
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> = _searchHistory

    init {
        observeData()
    }

    fun insertLocation(location: Location) {
        model.insertLocation(location)
    }

    fun searchLocation(locName: String, isExactMatch: Boolean) {
        _searchResult.value = model.getSearchedLocation(locName, isExactMatch)
    }

    fun getAllLocation(): List<Location> {
        return model.getAllLocation()
    }

    fun deleteHistory(historyName: String) {
        model.deleteHistory(historyName)
        _searchHistory.value = model.getAllHistory()
    }

    fun insertHistory(historyName: String) {
        model.insertHistory(historyName)
        _searchHistory.value = model.getAllHistory()
    }

    fun getAllHistory(): List<String> {
        return model.getAllHistory()
    }

    fun searchByKeywordFromServer(keyword: String, isExactMatch: Boolean) {
        model.searchByKeywordFromServer(keyword, isExactMatch)
    }

    private fun observeData() {
        model.searchResult.observeForever(Observer {
            _searchResult.value = it
        })
        model.searchHistory.observeForever(Observer {
            _searchHistory.value = it
        })
    }
}