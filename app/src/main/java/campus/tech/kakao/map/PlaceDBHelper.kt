package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class PlaceDBHelper(context: Context) : SQLiteOpenHelper(context, "Place.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        createPlaceTable(db)
        insertInitialPlaceData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS PlaceTable")
        onCreate(db)
    }

    private fun createPlaceTable(db: SQLiteDatabase?) {
        val placeTableSQL = """
            CREATE TABLE IF NOT EXISTS PlaceTable (
                idx INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                address TEXT NOT NULL
            )
        """
        db?.execSQL(placeTableSQL)
    }

    private fun insertInitialPlaceData(db: SQLiteDatabase?) {
        val initialPlaces = listOf(
            Place(1, "카카오 카페", "카페", "금정구 1"),
            Place(2, "카카오 식당", "식당", "금정구 2"),
            Place(3, "카카오 포차", "주점", "금정구 3"),
            Place(4, "테크 카페", "카페", "금정구 4"),
            Place(5, "테크 식당", "식당", "금정구 5"),
            Place(6, "테크 포차", "주점", "금정구 6"),
            Place(7, "캠퍼스 카페", "카페", "금정구 7"),
            Place(8, "캠퍼스 식당", "식당", "금정구 8"),
            Place(9, "캠퍼스 포차", "주점", "금정구 9")
        )

        initialPlaces.forEach { place ->
            val values = ContentValues().apply {
                put("name", place.name)
                put("category", place.category)
                put("address", place.address)
            }
            db?.insert("PlaceTable", null, values)
        }
    }

    fun getAllPlaces(): MutableList<Place> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM PlaceTable", null)
        val places = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            do {
                val idx = cursor.getInt(cursor.getColumnIndexOrThrow("idx"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                places.add(Place(idx, name, category, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return places
    }
}