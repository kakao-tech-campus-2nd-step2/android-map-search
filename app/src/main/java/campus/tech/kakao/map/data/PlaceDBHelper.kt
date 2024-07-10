package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.model.Place

class PlaceDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_ADDRESS TEXT, " +
                "$COLUMN_CATEGORY TEXT" +
                ")"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPlace(place: Place) {
        val db = writableDatabase
        val contentValues =
            ContentValues().apply {
                put(COLUMN_NAME, place.name)
                put(COLUMN_ADDRESS, place.address)
                put(COLUMN_CATEGORY, place.category)
            }
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun clearAllPlaces() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getPlacesByCategory(category: String): List<Place> {
        val db = readableDatabase
        val cursor =
            db.query(
                TABLE_NAME,
                null,
                "$COLUMN_CATEGORY=?",
                arrayOf(category),
                null,
                null,
                null,
            )
        val places = mutableListOf<Place>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            places.add(Place(id, name, address, category))
        }
        cursor.close()
        return places
    }

    companion object {
        const val DATABASE_NAME = "place.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "place_data"

        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
    }
}
