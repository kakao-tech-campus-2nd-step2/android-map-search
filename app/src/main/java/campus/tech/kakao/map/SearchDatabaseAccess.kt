package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class SearchDatabaseAccess(context: Context) {
    private val dbHelper = SearchDatabaseHelper(context)

    fun insertSearch(place: PlaceDataModel) {
        val db = dbHelper.writableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${PlaceContract.Place.TABLE_NAME} WHERE ${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(place.name))
        val values = ContentValues().apply {
            put(PlaceContract.Place.COLUMN_NAME, place.name)
            put(PlaceContract.Place.COLUMN_ADDRESS, place.address)
            put(PlaceContract.Place.COLUMN_CATEGORY, place.category)
        }

        if (cursor.moveToFirst()) {
            Log.d("tableName", PlaceContract.Place.TABLE_NAME)
            db.update(PlaceContract.Place.TABLE_NAME, values, "${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(place.name))
        }
        else {
            db.insert(PlaceContract.Place.TABLE_NAME, null, values)
        }
        cursor.close()
    }

    fun deleteSearch(name: String) {
        val db = dbHelper.writableDatabase
        db.delete(PlaceContract.Place.TABLE_NAME, "${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(name))
    }

    fun getAllSearch(): List<PlaceDataModel> {
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


}