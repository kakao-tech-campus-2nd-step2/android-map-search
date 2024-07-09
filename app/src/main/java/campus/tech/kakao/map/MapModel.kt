package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapModel(mContext: Context) {
    private val helper: MapDbHelper = MapDbHelper(mContext)
    private val retrofit: RetrofitService = RetrofitServiceClient.getRetrofit("https://dapi.kakao.com/")

    private val _searchResult = MutableLiveData(getAllLocation())
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory = MutableLiveData(getAllHistory())
    val searchHistory: LiveData<List<String>> = _searchHistory

    fun searchByKeywordFromServer(keyword: String, isExactMatch: Boolean) {
        val authorization = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        retrofit.requestLocationByKeyword(authorization, keyword).enqueue(object : Callback<ServerResult> {
            override fun onResponse(call: Call<ServerResult>, response: Response<ServerResult>) {
                if (response.isSuccessful) {
                    clearDb()
                    val body = response.body()
                    body?.let {
                        updateDb(body)
                    }
                }
            }

            override fun onFailure(call: Call<ServerResult>, response: Throwable) {
                Log.d("Model", "Fail")
            }
        })
    }
    fun insertLocation(location: Location) {
        val writableDb = helper.writableDatabase
        val content = ContentValues()
        content.put(MapContract.MapEntry.COLUMN_NAME_NAME, location.name)
        content.put(MapContract.MapEntry.COLUMN_NAME_CATEGORY, location.category)
        content.put(MapContract.MapEntry.COLUMN_NAME_ADDRESS, location.address)

        writableDb.insert(MapContract.MapEntry.TABLE_NAME, null, content)
    }

    fun getSearchedLocation(locName: String, isExactMatch: Boolean): List<Location> {
        val readableDb = helper.readableDatabase

        val selection = "${MapContract.MapEntry.COLUMN_NAME_NAME} LIKE ?"
        val selectionArgs = arrayOf("%${locName}%")
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        return getLocationResult(cursor)
    }

    fun getAllLocation(): List<Location> {
        val readableDb = helper.readableDatabase
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return getLocationResult(cursor)
    }

    private fun getLocationResult(cursor: Cursor): List<Location> {
        val res = mutableListOf<Location>()
        while (cursor.moveToNext()) {
            res.add(getLocation(cursor))
        }
        cursor.close()
        return res
    }

    private fun getLocation(cursor: Cursor): Location {
        val name =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_NAME))
        val category =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_CATEGORY))
        val address =
            cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_ADDRESS))

        return Location(name, category, address)
    }

    private fun getLocation(document: Document): Location {
        val name = document.placeName
        val category = document.categoryGroupName
        val address = document.roadAddressName

        return Location(name, category, address)
    }

    fun insertHistory(locName: String) {

        if (isHistoryExist(locName))
            deleteHistory(locName)
        val writeableDb = helper.writableDatabase
        val content = ContentValues()
        content.put(MapContract.MapEntry.COLUMN_NAME_NAME, locName)
        writeableDb.insert(MapContract.MapEntry.TABLE_NAME_HISTORY, null, content)
    }

    private fun isHistoryExist(locName: String): Boolean {
        val readableDb = helper.readableDatabase
        val selection = "${MapContract.MapEntry.COLUMN_NAME_NAME} = ?"
        val selectionArgs = arrayOf(locName)
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME_HISTORY,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val isExist: Boolean = cursor.moveToNext()
        cursor.close()
        return isExist
    }

    fun deleteHistory(locName: String) {
        val writeableDb = helper.writableDatabase
        val selection = "${MapContract.MapEntry.COLUMN_NAME_NAME} = ?"
        val selectionArgs = arrayOf(locName)

        writeableDb.delete(MapContract.MapEntry.TABLE_NAME_HISTORY, selection, selectionArgs)
    }

    fun getAllHistory(): List<String> {
        val readableDb = helper.readableDatabase
        val cursor = readableDb.query(
            MapContract.MapEntry.TABLE_NAME_HISTORY,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val res = mutableListOf<String>()
        while (cursor.moveToNext()) {
            res.add(cursor.getString(cursor.getColumnIndexOrThrow(MapContract.MapEntry.COLUMN_NAME_NAME)))
        }
        cursor.close()
        return res
    }

    private fun updateDb(serverResult: ServerResult) {
        Log.d("Model", serverResult.meta.toString())
        Log.d("Model", serverResult.docList.size.toString())
        Log.d("Model", "~~~~~")
        val res = mutableListOf<Location>()
        serverResult.docList.forEach { document ->
            val location = getLocation(document)
            insertLocation(location)
            res.add(location)
            Log.d("Model", document.toString())
        }
        Log.d("Model", "==============")
        _searchResult.value = res
    }

    private fun clearDb() {
        helper.clearDb(helper.writableDatabase)
    }
}