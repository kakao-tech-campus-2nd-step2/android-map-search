package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MapViewModel(mContext: Context, val databaseListener: DatabaseListener) : ViewModel() {
    private val model = MapModel(mContext)
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory: MutableLiveData<List<String>>
    val searchHistory: LiveData<List<String>>

    init {
        model.searchResult.observeForever(Observer {
            _searchResult.value = it
        })

//        model.s
//        _searchResult = MutableLiveData(model.getSearchedLocation("", false))
//        searchResult = _searchResult
        _searchHistory = MutableLiveData(model.getAllHistory())
        searchHistory = _searchHistory
    }

    fun insertLocation(location: Location) {
        model.insertLocation(location)
    }

    fun searchLocation(locName: String, isExactMatch: Boolean) {
        _searchResult.value = model.getSearchedLocation(locName, isExactMatch)
//        databaseListener.updateSearchResult()
    }

    fun getAllLocation(): List<Location> {
        return model.getAllLocation()
    }

    fun deleteHistory(historyName: String) {
        model.deleteHistory(historyName)
        _searchHistory.value = model.getAllHistory()
//        databaseListener.updateSearchHistory()
    }

    fun insertHistory(historyName: String) {
        model.insertHistory(historyName)
        _searchHistory.value = model.getAllHistory()
//        databaseListener.updateSearchHistory()
    }

    fun getAllHistory(): List<String> {
        return model.getAllHistory()
    }

    fun searchByKeyword(keyword: String, isExactMatch: Boolean) {
        model.searchByKeyword(keyword, isExactMatch)
//        _searchResult.value = model.getAllLocation()
//        databaseListener.updateSearchResult()
    }
}