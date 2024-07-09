package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object MapContract {
    const val TABLE_CAFE = "cafe"
    const val TABLE_PHARMACY = "pharmacy"
    const val COLUMN_NAME = "name"
    const val COLUMN_ADDRESS = "address"
    const val COLUMN_CATEGORY = "category"
}

class Database(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_CAFE} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${MapContract.TABLE_PHARMACY} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        addDummyData(db)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_CAFE}")
        db?.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_PHARMACY}")
        onCreate(db)
    }

    private fun addDummyData(db: SQLiteDatabase?) {
        addDummyCafes(db)
        addDummyPharmacies(db)
    }

    private fun addDummyCafes(db: SQLiteDatabase?) {
        val cafes = mutableListOf<Array<String>>()
        for (i in 1..20) {
            cafes.add(arrayOf("카페 $i", "광주 북구 용봉동 $i", "카페"))
        }

        cafes.forEach { data ->
            val values = ContentValues().apply {
                put(MapContract.COLUMN_NAME, data[0])
                put(MapContract.COLUMN_ADDRESS, data[1])
                put(MapContract.COLUMN_CATEGORY, data[2])
            }
            db?.insert(MapContract.TABLE_CAFE, null, values)
        }
    }

    private fun addDummyPharmacies(db: SQLiteDatabase?) {
        val pharmacies = mutableListOf<Array<String>>()
        for (i in 1..20) {
            pharmacies.add(arrayOf("약국 $i", "경상남도 함안군 칠원읍 ${i + 20}", "약국"))
        }

        pharmacies.forEach { data ->
            val values = ContentValues().apply {
                put(MapContract.COLUMN_NAME, data[0])
                put(MapContract.COLUMN_ADDRESS, data[1])
                put(MapContract.COLUMN_CATEGORY, data[2])
            }
            db?.insert(MapContract.TABLE_PHARMACY, null, values)
        }
    }

    fun searchPlaces(searchText: String): List<Map<String, String>> {
        val db = readableDatabase
        val cafes = queryPlaces(db, MapContract.TABLE_CAFE, searchText)
        val pharmacies = queryPlaces(db, MapContract.TABLE_PHARMACY, searchText)
        db.close()

        val dataList = mutableListOf<Map<String, String>>()
        dataList.addAll(cafes)
        dataList.addAll(pharmacies)
        return dataList
    }

    private fun queryPlaces(
        db: SQLiteDatabase,
        tableName: String,
        searchText: String
    ): List<Map<String, String>> {
        val cursor: Cursor = db.query(
            tableName,
            null,
            "${MapContract.COLUMN_NAME} LIKE ? OR ${MapContract.COLUMN_ADDRESS} LIKE ?",
            arrayOf("%$searchText%", "%$searchText%"),
            null,
            null,
            null
        )

        val results = mutableListOf<Map<String, String>>()
        while (cursor.moveToNext()) {
            val row = mutableMapOf<String, String>()
            row[MapContract.COLUMN_NAME] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_NAME))
            row[MapContract.COLUMN_ADDRESS] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_ADDRESS))
            row[MapContract.COLUMN_CATEGORY] = cursor.getString(cursor.getColumnIndexOrThrow(MapContract.COLUMN_CATEGORY))
            results.add(row)
        }
        cursor.close()
        return results
    }
}