package campus.tech.kakao.map.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.dto.SearchResponse
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.RecentSearchWord
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapRepository(private val context: Context) {
    private val localDB: PlacesDBHelper = PlacesDBHelper(context)

    private lateinit var prefs: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var stringPrefs: String? = null
    var searchHistoryList = ArrayList<RecentSearchWord>()

    private val _places: MutableLiveData<List<Place>> = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    init {
        setPrefs()
        val dbFile = context.getDatabasePath("${PlacesDBHelper.TABLE_NAME}")
        if (!dbFile.exists()) {
            insertLocalInitialData()
        }
    }


    /**
     * 카카오 REST API 관련
     */
    fun searchPlaces(search: String) {
        if (search.isEmpty()) {
            _places.value = mutableListOf()
            return
        }
        RetrofitClient.retrofitService.requestPlaces(query = search).enqueue(object :
            Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val responseList = mutableListOf<Place>()
                    body?.documents?.forEach {
                        val category = it.categoryName.split(" \u003e ").last()
                        responseList.add(Place(it.placeName, it.addressName, category))
                    }
                    _places.value = responseList.toMutableList()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                println("error: $t")
            }
        })
    }



    /**
     * Local DB 관련
     */
    private fun insertLocalInitialData() {
        val places = arrayListOf<Place>()
        for (i in 1..30) {
            val cafe = Place("카페$i", "서울 성동구 성수동 $i", "카페")
            val pharmacy = Place("약국$i", "서울 강남구 대치동 $i", "약국")
            places.add(cafe)
            places.add(pharmacy)
        }
        localDB.insertPlaces(places)
    }

    fun getAllLocalPlaces(): List<Place> {
        return localDB.getAllPlaces()
    }

    fun insertLocalPlace(name: String, address: String, category: String) {
        val place = Place(name, address, category)
        localDB.insertPlace(place)
    }

    fun deleteLocalPlace(name: String, address: String, category: String) {
        val place = Place(name, address, category)
        localDB.deletePlace(place)
    }

    fun searchDBPlaces(search: String) {
        val allPlaces = getAllLocalPlaces()
        val filtered = if (search.isEmpty()) {
            emptyList()
        } else {
            allPlaces.filter { it.name.contains(search, ignoreCase = true) }
        }
        Log.d("Thread", "${Thread.currentThread().name}")   // main 스레드
        _places.value = filtered
    }



    /**
     * Search History 관련
     */
    fun getSearchHistory(): ArrayList<RecentSearchWord> {
        return searchHistoryList
    }

    fun moveSearchToLast(idx: Int, search: String) {
        searchHistoryList.removeAt(idx)
        searchHistoryList.add(RecentSearchWord(search))
        saveSearchHistory()
    }

    fun addSearchHistory(search: String) {
        searchHistoryList.add(RecentSearchWord(search))
        saveSearchHistory()
    }

    fun delSearchHistory(idx: Int) {
        searchHistoryList.removeAt(idx)
        saveSearchHistory()
    }

    private fun saveSearchHistory() {
        stringPrefs = GsonBuilder().create().toJson(
            searchHistoryList, object : TypeToken<ArrayList<RecentSearchWord>>() {}.type
        )
        prefEditor.putString(SEARCH_HISTORY, stringPrefs)
        prefEditor.apply()
    }

    private fun setPrefs() {
        prefs = context.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        prefEditor = prefs.edit()
        stringPrefs = prefs.getString(SEARCH_HISTORY, null)

        if (stringPrefs != null && stringPrefs != "[]") {
            searchHistoryList = GsonBuilder().create().fromJson(
                stringPrefs, object : TypeToken<ArrayList<RecentSearchWord>>() {}.type
            )
        }
    }

    companion object {
        private const val PREF_NAME = "app_data"
        private const val SEARCH_HISTORY = "search_history"
    }

}