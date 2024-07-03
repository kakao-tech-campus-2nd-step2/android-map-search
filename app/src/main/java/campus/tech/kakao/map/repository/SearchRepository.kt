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

    suspend fun searchPlaces(keyword: String): List<PlaceData> {
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
