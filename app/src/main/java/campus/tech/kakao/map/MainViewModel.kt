package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(context: Context) : ViewModel() {
    private val dbHelper: DBHelper = DBHelper(context)
    private val db = dbHelper.writableDatabase
    private val preferenceManager = MapApplication.prefs

    private var _placeList = MutableLiveData<List<Place>>()
    private val _searchHistoryList = MutableLiveData<List<SearchHistory>>()

    init {
        _searchHistoryList.value = getSearchHistory()
    }

    val searchHistoryList: LiveData<List<SearchHistory>>
        get() = _searchHistoryList

    val placeList: LiveData<List<Place>>
        get() = _placeList

    fun insertPlace(place: Place) {
        dbHelper.insert(db, place)
    }

    override fun onCleared() {
        super.onCleared()
        if (db.isOpen) db.close()
    }

    fun getSearchResult(searchText: String) {
        if (searchText.isEmpty()) {
            _placeList.postValue(emptyList())
        } else {
            val rDb = dbHelper.readableDatabase
            val places = mutableListOf<Place>()
            val query = "SELECT * FROM ${PlaceContract.TABLE_NAME} WHERE ${PlaceContract.TABLE_COLUMN_NAME} LIKE ?"
            val cursor = rDb.rawQuery(query, arrayOf("%$searchText%"))

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.TABLE_COLUMN_NAME))
                        val address = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.TABLE_COLUMN_ADDRESS))
                        val category = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.TABLE_COLUMN_CATEGORY))
                        val place = Place(name, address, category)
                        places.add(place)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
            _placeList.postValue(places)
        }
    }

    fun getSearchHistoryList() {
        _searchHistoryList.value = getSearchHistory()
    }

    private fun getSearchHistory(): ArrayList<SearchHistory> {
        return preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY)
    }

    fun saveSearchHistory(searchHistory: SearchHistory) {
        val currentList = getSearchHistory()
        preferenceManager.savePreference(Constants.SEARCH_HISTORY_KEY, searchHistory, currentList)
        getSearchHistoryList()
    }

    fun deleteSearchHistory(position: Int) {
        preferenceManager.deleteArrayListItem(Constants.SEARCH_HISTORY_KEY, position)
        getSearchHistoryList()
    }
}
