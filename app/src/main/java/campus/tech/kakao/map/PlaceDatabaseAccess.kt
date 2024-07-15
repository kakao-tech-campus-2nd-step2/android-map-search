package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class PlaceDatabaseAccess(context: Context, databaseName: String) {
    private val dbHelper = PlaceDatabaseHelper(context, databaseName)

    fun insertPlace(place: PlaceDataModel) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(PlaceContract.Place.COLUMN_NAME, place.name)
            put(PlaceContract.Place.COLUMN_ADDRESS, place.address)
            put(PlaceContract.Place.COLUMN_CATEGORY, place.category)
        }
        db.insert(PlaceContract.Place.TABLE_NAME, null, values)
    }

    fun deletePlace(name: String) {
        val db = dbHelper.writableDatabase
        db.delete(PlaceContract.Place.TABLE_NAME, "${PlaceContract.Place.COLUMN_NAME} = ?", arrayOf(name))
    }

    fun getAllPlace(): MutableList<PlaceDataModel> {
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