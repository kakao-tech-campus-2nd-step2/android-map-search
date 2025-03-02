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

    private val context: Context = context.applicationContext
    private fun insertInitialPlaceData(db: SQLiteDatabase?) {
        val initialPlaces = listOf(
            Place(1, context.getString(R.string.place_kakao_cafe), context.getString(R.string.category_cafe), context.getString(R.string.place_address_1)),
            Place(2, context.getString(R.string.place_kakao_restaurant), context.getString(R.string.category_restaurant), context.getString(R.string.place_address_2)),
            Place(3, context.getString(R.string.place_kakao_pub), context.getString(R.string.category_pub), context.getString(R.string.place_address_3)),
            Place(4, context.getString(R.string.place_tech_cafe), context.getString(R.string.category_cafe), context.getString(R.string.place_address_4)),
            Place(5, context.getString(R.string.place_tech_restaurant), context.getString(R.string.category_restaurant), context.getString(R.string.place_address_5)),
            Place(6, context.getString(R.string.place_tech_pub), context.getString(R.string.category_pub), context.getString(R.string.place_address_6)),
            Place(7, context.getString(R.string.place_campus_cafe), context.getString(R.string.category_cafe), context.getString(R.string.place_address_7)),
            Place(8, context.getString(R.string.place_campus_restaurant), context.getString(R.string.category_restaurant), context.getString(R.string.place_address_8)),
            Place(9, context.getString(R.string.place_campus_pub), context.getString(R.string.category_pub), context.getString(R.string.place_address_9))
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