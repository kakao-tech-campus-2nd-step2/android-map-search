package campus.tech.kakao.map

import android.content.ContentValues
import android.util.Log
import campus.tech.kakao.map.Contract.LocationEntry
import campus.tech.kakao.map.Contract.SavedLocationEntry

class LocationLocalRepository(private val dbHelper : LocationDbHelper) {

    fun insertLocation(title: String, address: String, category: String): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(LocationEntry.COLUMN_NAME_TITLE, title)
            put(LocationEntry.COLUMN_NAME_ADDRESS, address)
            put(LocationEntry.COLUMN_NAME_CATEGORY, category)
        }

        return db.insert(LocationEntry.TABLE_NAME, null, values)
    }

    fun getLocationAll(): MutableList<Location> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            LocationEntry.COLUMN_NAME_TITLE,
            LocationEntry.COLUMN_NAME_ADDRESS,
            LocationEntry.COLUMN_NAME_CATEGORY
        )

        val sortOrder = "${LocationEntry.COLUMN_NAME_TITLE} ASC"

        val cursor = db.query(
            LocationEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val results = mutableListOf<Location>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_TITLE))
                val address = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_ADDRESS))
                val category = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_CATEGORY))
                results.add(Location(title, address, category))
            }
        }
        cursor.close()
        return results
    }

    fun insertSavedLocation(title: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SavedLocationEntry.COLUMN_NAME_TITLE, title)
        }
        Log.d("jieun", "insertSavedLocation 저장완료")
        return db.insert(SavedLocationEntry.TABLE_NAME, null, values)
    }

    fun getSavedLocationAll(): MutableList<SavedLocation> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            SavedLocationEntry.COLUMN_NAME_TITLE
        )
        val sortOrder = "${SavedLocationEntry.COLUMN_NAME_TITLE} ASC"
        val cursor = db.query(
            SavedLocationEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val results = mutableListOf<SavedLocation>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(SavedLocationEntry.COLUMN_NAME_TITLE))
                results.add(SavedLocation(title))
            }
        }
        cursor.close()
        return results
    }

    fun deleteSavedLocation(title: String) {
        val db = dbHelper.writableDatabase

        val selection = "${SavedLocationEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf(title)

        db.delete(SavedLocationEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun searchLocations(query: String): List<Location> {
        if(query.isBlank()){
            return emptyList()
        }
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            LocationEntry.COLUMN_NAME_TITLE,
            LocationEntry.COLUMN_NAME_ADDRESS,
            LocationEntry.COLUMN_NAME_CATEGORY
        )

        val selection = "${LocationEntry.COLUMN_NAME_TITLE} LIKE '%' || ? || '%' OR ${LocationEntry.COLUMN_NAME_ADDRESS} LIKE '%' || ? || '%' OR ${LocationEntry.COLUMN_NAME_CATEGORY} LIKE '%' || ? || '%'"
        val selectionArgs = arrayOf(query, query, query)

        val cursor = db.query(
            LocationEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val results = mutableListOf<Location>()
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_TITLE))
                val address = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_ADDRESS))
                val category = getString(getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_CATEGORY))
                results.add(Location(title, address, category))
            }
        }
        cursor.close()
        return results
    }
}
