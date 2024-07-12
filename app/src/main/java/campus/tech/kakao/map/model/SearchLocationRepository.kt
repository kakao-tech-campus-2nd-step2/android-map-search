package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import campus.tech.kakao.map.BuildConfig
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL
import java.lang.Exception

class SearchLocationRepository(context: Context) {
    private val historyDbHelper: HistoryDbHelper = HistoryDbHelper(context)

    fun searchLocation(category: String, callback: LocationCallBack) {
        val connection = URL(
            "https://dapi.kakao.com/v2/local/search/keyword.json" +
                    "?query=${category}" + "&page=1" + "&size=15"
        ).openConnection() as HttpURLConnection
        connection.setRequestProperty("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
        connection.requestMethod = "GET"

        Thread {
            try {
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = Gson().fromJson(
                        connection.inputStream.bufferedReader().use { it.readText() },
                        LocalSearchResponse::class.java
                    )
                    val resultList = response.documents.map {
                        Location(
                            name = it.place_name,
                            address = it.address_name,
                            category = it.category_group_name
                        )
                    }
                    callback.onSuccess(resultList)
                } else {
                    callback.onFailure(Exception("HTTP response code: ${connection.responseCode}"))
                }
            } catch (e: Exception) {
                callback.onFailure(e)
            }
        }.start()
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