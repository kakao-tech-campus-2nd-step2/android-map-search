package campus.tech.kakao.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.RetrofitInstance.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(context: Context) : ViewModel() {
    private val dbHelper: DBHelper = DBHelper(context)
    private val db = dbHelper.writableDatabase
    private val preferenceManager = MapApplication.prefs

    private var _placeList = MutableLiveData<List<Place>>()
    private val _searchHistoryList = MutableLiveData<List<SearchHistory>>()
    private var _locationList = MutableLiveData<List<Document>>()

    init {
        _searchHistoryList.value = getSearchHistory()
    }

    val searchHistoryList: LiveData<List<SearchHistory>>
        get() = _searchHistoryList

    val placeList: LiveData<List<Place>>
        get() = _placeList

    val locationList: LiveData<List<Document>>
        get() = _locationList

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

    fun getPlace(query: String) {
        if (query.isEmpty()) {
            _locationList.value = emptyList()
        } else {
            retrofitService.getPlaces("KakaoAK "+BuildConfig.KAKAO_REST_API_KEY, query).enqueue(object : Callback<Location> {
                override fun onResponse(
                    call: Call<Location>,
                    response: Response<Location>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _locationList.postValue(body.documents)
                            Log.d("성공", ""+ body.documents)
                        } else {
                            _locationList.postValue(emptyList())
                        }
                    } else {
                        Log.d("태그",response.code().toString())
                    }
                }

                override fun onFailure(call: Call<Location>, t: Throwable) {
                    Log.d("error", ""+ t)
                }
            })
        }
    }
}
