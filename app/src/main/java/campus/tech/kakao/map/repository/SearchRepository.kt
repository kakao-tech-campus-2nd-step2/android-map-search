package campus.tech.kakao.map.repository

import android.content.ContentValues
import android.content.Context
import campus.tech.kakao.map.model.DatabaseHelper
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.PlaceData
import campus.tech.kakao.map.model.Search
import campus.tech.kakao.map.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}
