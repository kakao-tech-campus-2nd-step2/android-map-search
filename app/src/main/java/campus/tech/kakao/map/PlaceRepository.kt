package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import androidx.core.content.edit
import campus.tech.kakao.map.domain.model.Place

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDBHelper.getInstance(context)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("search_history", Context.MODE_PRIVATE)

    init {
        insertDummies()
    }

    fun insertPlace(place: Place) {
        val db = dbHelper.writableDatabase
        val values =
            ContentValues().apply {
                put(PlaceDBContract.PlaceEntry.COLUMN_NAME, place.placeName)
                put(PlaceDBContract.PlaceEntry.COLUMN_ADDRESS, place.addressName)
                put(PlaceDBContract.PlaceEntry.COLUMN_TYPE, place.categoryName)
            }
        db.insert(PlaceDBContract.PlaceEntry.TABLE_NAME, null, values)
        db.close()
    }

    fun getAllPlaces(): MutableList<Place> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor =
            db.query(
                PlaceDBContract.PlaceEntry.TABLE_NAME,
                arrayOf(
                    PlaceDBContract.PlaceEntry.COLUMN_NAME,
                    PlaceDBContract.PlaceEntry.COLUMN_ADDRESS,
                    PlaceDBContract.PlaceEntry.COLUMN_TYPE,
                ),
                null,
                null,
                null,
                null,
                null,
            )

        val places = mutableListOf<Place>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_NAME))
                val address =
                    getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_ADDRESS))
                val type = getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_TYPE))
                places.add(Place(name, address, type))
            }
            close()
        }
        // db.close()
        return places
    }

    fun insertDummies() {
        clearPlaces()

        val db = dbHelper.writableDatabase
        val dummyPlaces = (
            (1..30).map {
                Place("카페 $it", "대전 서구 만년동 $it", "카페")
            }
        )
        db.beginTransaction()
        for (place in dummyPlaces) {
            val value =
                ContentValues().apply {
                    put(PlaceDBContract.PlaceEntry.COLUMN_NAME, place.placeName)
                    put(PlaceDBContract.PlaceEntry.COLUMN_ADDRESS, place.addressName)
                    put(PlaceDBContract.PlaceEntry.COLUMN_TYPE, place.categoryName)
                }
            db.insert(PlaceDBContract.PlaceEntry.TABLE_NAME, null, value)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    private fun clearPlaces() {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.delete(PlaceDBContract.PlaceEntry.TABLE_NAME, null, null)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        db.close()
    }

    fun searchPlaces(query: String): MutableList<Place> {
        val searchedPlaces = mutableListOf<Place>()
        val db = dbHelper.readableDatabase
        // Selection : WHERE 절에 들어가는 조건을 쓴다.
        val selection = "${PlaceDBContract.PlaceEntry.COLUMN_NAME} LIKE ?"
        // Selection Arguments : 조건에 대한 값
        val selectionArgs = arrayOf("%$query%")

        val cursor: Cursor =
            db.query(
                PlaceDBContract.PlaceEntry.TABLE_NAME,
                arrayOf(
                    PlaceDBContract.PlaceEntry.COLUMN_NAME,
                    PlaceDBContract.PlaceEntry.COLUMN_ADDRESS,
                    PlaceDBContract.PlaceEntry.COLUMN_TYPE,
                ),
                selection,
                selectionArgs,
                null,
                null,
                null,
            )

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_NAME))
                val address =
                    getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_ADDRESS))
                val type = getString(getColumnIndexOrThrow(PlaceDBContract.PlaceEntry.COLUMN_TYPE))
                searchedPlaces.add(Place(name, address, type))
            }
            close()
        }
        db.close()
        return searchedPlaces
    }

    fun getSearchHistory(): Set<String> {
        return sharedPreferences.getStringSet("history", setOf()) ?: setOf()
    }

    fun saveSearchHistory(query: String) {
        val currentHistory = getSearchHistory().toMutableList()
        currentHistory.add(query)
        sharedPreferences.edit {
            putStringSet("history", currentHistory.toSet())
        }
    }

    fun removeSearchQuery(query: String) {
        val currentHistory = getSearchHistory().toMutableList()
        if (currentHistory.contains(query)) {
            currentHistory.remove(query)
            sharedPreferences.edit {
                putStringSet("history", currentHistory.toSet())
            }
        }
    }
}
