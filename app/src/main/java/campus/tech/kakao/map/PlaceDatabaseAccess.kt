package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context

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
}