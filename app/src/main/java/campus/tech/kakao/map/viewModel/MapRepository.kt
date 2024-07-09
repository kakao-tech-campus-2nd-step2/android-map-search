package campus.tech.kakao.map.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.dto.SearchResponse
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.RecentSearchWord
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapRepository(private val context: Context) {
    private val localDB: PlacesDBHelper = PlacesDBHelper(context)

    private lateinit var prefs: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private var stringPrefs: String? = null
    private var searchHistoryList = ArrayList<RecentSearchWord>()

    init {
        val dbFile = context.getDatabasePath("${PlacesDBHelper.TABLE_NAME}")
        if (!dbFile.exists()) {
            insertLocalInitialData()
        }
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



    /**
     * Search History 관련
     */

    fun getSearchHistory(): ArrayList<RecentSearchWord> {
        setPrefs()
        return searchHistoryList
    }

    fun saveSearchHistory() {
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