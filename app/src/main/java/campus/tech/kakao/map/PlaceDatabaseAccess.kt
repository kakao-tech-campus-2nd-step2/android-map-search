package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class PlaceDatabaseAccess(context: Context) {
    private val dbHelper = PlaceDatabaseHelper(context)

    fun insertPlace(name: String, address: String, category: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlaceContract.Place.COLUMN_NAME, name)
            put(PlaceContract.Place.COLUMN_ADDRESS, address)
            put(PlaceContract.Place.COLUMN_CATEGORY, category)
        }
        db.insert(PlaceContract.Place.TABLE_NAME, null, values)
    }

    fun deletePlace(name: String) {
        val db = dbHelper.writableDatabase
        db.delete(PlaceContract.Place.TABLE_NAME, "${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(name))
    }

    fun getAllPlace(): List<PlaceDataModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${PlaceContract.Place.TABLE_NAME}", null)
        val dataList = mutableListOf<PlaceDataModel>()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_NAME))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_ADDRESS))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_CATEGORY))
                dataList.add(PlaceDataModel(name, address, category))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }

    fun searchPlace(keyword: String): List<PlaceDataModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${PlaceContract.Place.TABLE_NAME} WHERE ${PlaceContract.Place.COLUMN_NAME} LIKE ?", arrayOf("%$keyword%"))
        val dataList = mutableListOf<PlaceDataModel>()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_NAME))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_ADDRESS))
                val category = cursor.getString(cursor.getColumnIndexOrThrow(PlaceContract.Place.COLUMN_CATEGORY))
                dataList.add(PlaceDataModel(name, address, category))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return dataList
    }

    fun hasData(): Boolean {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM ${PlaceContract.Place.TABLE_NAME}", null)
        var hasData = false
        if (cursor.moveToFirst()) {
            hasData = cursor.getInt(0) > 0
        }
        cursor.close()
        return hasData
    }
}