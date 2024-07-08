package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context

class SearchLocationRepository(context: Context) {
    private val locationDbHelper: LocationDbHelper = LocationDbHelper(context)
    private val historyDbHelper: HistoryDbHelper = HistoryDbHelper(context)

    fun searchLocation(category: String): List<Location> {
        val db = locationDbHelper.readableDatabase
        val searchQuery = "SELECT * FROM ${LocationContract.TABLE_NAME} " +
                "WHERE ${LocationContract.COLUMN_CATEGORY} = '$category'"
        val cursor = db.rawQuery(searchQuery, null)

        val result = mutableListOf<Location>()
        while (cursor.moveToNext()) {
            result.add(
                Location(
                    name = cursor.getString(cursor.getColumnIndexOrThrow(LocationContract.COLUMN_NAME)),
                    address = cursor.getString(cursor.getColumnIndexOrThrow(LocationContract.COLUMN_ADDRESS)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(LocationContract.COLUMN_CATEGORY))
                )
            )
        }
        cursor.close()
        db.close()

        return result.toList()
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