package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchLocationRepository(context: Context) {
    private val historyDbHelper: HistoryDbHelper = HistoryDbHelper(context)
    private val localSearchService = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/v2/local/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LocalSearchService::class.java)

    private val _searchResultLiveData = MutableLiveData<List<Location>>()
    val searchResultLiveData: LiveData<List<Location>> = _searchResultLiveData

    fun searchLocation(category: String) {
        localSearchService.requestLocalSearch(query = category)
            .enqueue(object : Callback<LocalSearchResponse> {
                override fun onResponse(
                    call: Call<LocalSearchResponse>,
                    response: Response<LocalSearchResponse>
                ) {
                    val searchResult = response.body()?.documents?.map {
                        Location(
                            name = it.place_name,
                            address = it.address_name,
                            category = it.category_group_name
                        )
                    } ?: emptyList()
                    _searchResultLiveData.value = searchResult
                }

                override fun onFailure(call: Call<LocalSearchResponse>, th: Throwable) {
                    Log.e("SearchLocationRepository", "Failed to search location", th)
                    _searchResultLiveData.value = emptyList()
                }
            })
    }

    fun addHistory(locationName: String) {
        if (isExistHistory(locationName)) {
            removeHistory(locationName)
        }

        val db = historyDbHelper.writableDatabase
        val historyValues = ContentValues()
        historyValues.put(HistoryContract.COLUMN_NAME, locationName)
        db.insert(HistoryContract.TABLE_NAME, null, historyValues)

        db.close()
    }

    fun getHistory(): List<String> {
        val db = historyDbHelper.readableDatabase
        val searchQuery = "SELECT * FROM ${HistoryContract.TABLE_NAME}"
        val cursor = db.rawQuery(searchQuery, null)

        val result = mutableListOf<String>()
        while (cursor.moveToNext()) {
            result.add(cursor.getString(cursor.getColumnIndexOrThrow(HistoryContract.COLUMN_NAME)))
        }

        cursor.close()
        db.close()
        return result.toList()
    }

    private fun isExistHistory(locationName: String): Boolean {
        val db = historyDbHelper.readableDatabase
        val searchQuery = "SELECT * FROM ${HistoryContract.TABLE_NAME} " +
                "WHERE ${HistoryContract.COLUMN_NAME} = '$locationName'"
        val cursor = db.rawQuery(searchQuery, null)

        val result = cursor.count > 0
        cursor.close()
        db.close()

        return result
    }

    fun removeHistory(locationName: String) {
        val db = historyDbHelper.writableDatabase
        val deleteQuery = "DELETE FROM ${HistoryContract.TABLE_NAME} " +
                "WHERE ${HistoryContract.COLUMN_NAME} = '$locationName'"
        db.execSQL(deleteQuery)
        db.close()
    }
}