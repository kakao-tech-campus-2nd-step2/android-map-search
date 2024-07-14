package campus.tech.kakao.map.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import campus.tech.kakao.map.api.RetrofitInstance
import campus.tech.kakao.map.model.DatabaseHelper
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.PlaceData
import campus.tech.kakao.map.model.Search
import campus.tech.kakao.map.model.SearchResponse
import campus.tech.kakao.map.model.SearchResult
import campus.tech.kakao.map.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class SearchRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    suspend fun getAllSearchResults(): List<SearchResult> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${Search.TABLE_NAME} ORDER BY ${Search.COLUMN_TIMESTAMP} DESC", null)

            val results = mutableListOf<SearchResult>()
            if (cursor.moveToFirst()) {
                do {
                    val result = SearchResult(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Search.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Search.COLUMN_KEYWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Search.COLUMN_TIMESTAMP))
                    )
                    results.add(result)
                } while (cursor.moveToNext())
            }
            cursor.close()
            results
        }
    }

    suspend fun addSearchResult(keyword: String) {
        withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Search.COLUMN_KEYWORD, keyword)
            }
            db.insert(Search.TABLE_NAME, null, values)
        }
    }

    suspend fun updateSearchResult(keyword: String) {
        withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val sql = "UPDATE ${Search.TABLE_NAME} SET ${Search.COLUMN_TIMESTAMP} = CURRENT_TIMESTAMP WHERE ${Search.COLUMN_KEYWORD} = ?"
            db.execSQL(sql, arrayOf(keyword))
        }
    }

    suspend fun addOrUpdateSearchResult(keyword: String) {
        withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                Search.TABLE_NAME,
                arrayOf(Search.COLUMN_ID),
                "${Search.COLUMN_KEYWORD} = ?",
                arrayOf(keyword),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                cursor.close()
                updateSearchResult(keyword)
            } else {
                addSearchResult(keyword)
            }
        }
    }

    suspend fun deleteSearchResult(id: Int) {
        withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            db.delete(Search.TABLE_NAME, "${Search.COLUMN_ID} = ?", arrayOf(id.toString()))
        }
    }

    suspend fun getAllPlaces(): List<PlaceData> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${Place.TABLE_NAME}", null)
            val places = mutableListOf<PlaceData>()

            if (cursor.moveToFirst()) {
                do {
                    val place = PlaceData(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Place.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_CATEGORY))
                    )
                    places.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
            places
        }
    }

    suspend fun addPlace(name: String, location: String, category: String) {
        withContext(Dispatchers.IO) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(Place.COLUMN_NAME, name)
                put(Place.COLUMN_LOCATION, location)
                put(Place.COLUMN_CATEGORY, category)
            }
            db.insert(Place.TABLE_NAME, null, values)
        }
    }


    fun searchPlaces(keyword: String, callback: (List<PlaceData>) -> Unit) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        val maxPages = 3
        val pageSize = 15

        val allPlaces = mutableListOf<PlaceData>()
        val apiService = RetrofitInstance.apiService

        fun fetchPage(page: Int) {
            apiService.searchPlaces(apiKey, keyword, page, pageSize).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if (response.isSuccessful) {
                        val places = response.body()?.documents?.map {
                            PlaceData(it.id, it.place_name, it.address_name, it.category_group_name)
                        } ?: emptyList()

                        allPlaces.addAll(places)

                        if (page < maxPages && places.isNotEmpty()) {
                            fetchPage(page + 1) // 다음 페이지 요청
                        } else {
                            callback(allPlaces)
                        }
                    } else {
                        Log.e("SearchRepository", "API call failed: ${response.errorBody()?.string()}")
                        callback(allPlaces)
                    }
                }
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Log.e("SearchRepository", "API call failed: ${t.message}")
                    callback(allPlaces)
                }
            })
        }
        fetchPage(1)
    }


    suspend fun searchPlacesFromDB(keyword: String): List<PlaceData> {
        return withContext(Dispatchers.IO) {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                Place.TABLE_NAME,
                null,
                "${Place.COLUMN_NAME} LIKE ? OR ${Place.COLUMN_CATEGORY} LIKE ?",
                arrayOf("%$keyword%", "%$keyword%"),
                null,
                null,
                null
            )
            val places = mutableListOf<PlaceData>()
            if (cursor.moveToFirst()) {
                do {
                    val place = PlaceData(
                        cursor.getInt(cursor.getColumnIndexOrThrow(Place.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Place.COLUMN_CATEGORY))
                    )
                    places.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
            places
        }
    }

}
