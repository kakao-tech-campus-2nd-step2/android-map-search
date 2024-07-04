package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
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
            "CREATE TABLE ${MapContract.TABLE_CAFE} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
        db?.execSQL(
            "CREATE TABLE ${MapContract.TABLE_PHARMACY} (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${MapContract.COLUMN_NAME} TEXT," +
                    "${MapContract.COLUMN_ADDRESS} TEXT," +
                    "${MapContract.COLUMN_CATEGORY} TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
    //STEP1 과제 더미 데이터 생성
    fun insertData() {
        val db = writableDatabase

        for (i in 1..100) {
            val values = ContentValues().apply {
                put(MapContract.COLUMN_NAME, "카페$i")
                put(MapContract.COLUMN_ADDRESS, "광주 북구 용봉동 $i")
                put(MapContract.COLUMN_CATEGORY, "카페")
            }
            db.insert(MapContract.TABLE_CAFE, null, values)

            val values2 = ContentValues().apply {
                put(MapContract.COLUMN_NAME, "약국$i")
                put(MapContract.COLUMN_ADDRESS, "경상남도 마산시 합성동 $i")
                put(MapContract.COLUMN_CATEGORY, "약국")
            }
            db.insert(MapContract.TABLE_PHARMACY, null, values2)
        }

        db.close()
    }

    fun getAllData(): List<Map<String, String>> {
        val db = readableDatabase
        val cafeCursor = db.query(MapContract.TABLE_CAFE, null, null, null, null, null, null)
        val pharmacyCursor = db.query(MapContract.TABLE_PHARMACY, null, null, null, null, null, null)

        val dataList = mutableListOf<Map<String, String>>()

        with(cafeCursor) {
            while (moveToNext()) {
                val data = mapOf(
                    MapContract.COLUMN_NAME to getString(getColumnIndexOrThrow(MapContract.COLUMN_NAME)),
                    MapContract.COLUMN_ADDRESS to getString(getColumnIndexOrThrow(MapContract.COLUMN_ADDRESS)),
                    MapContract.COLUMN_CATEGORY to getString(getColumnIndexOrThrow(MapContract.COLUMN_CATEGORY))
                )
                dataList.add(data)
            }
            close()
        }

        with(pharmacyCursor) {
            while (moveToNext()) {
                val data = mapOf(
                    MapContract.COLUMN_NAME to getString(getColumnIndexOrThrow(MapContract.COLUMN_NAME)),
                    MapContract.COLUMN_ADDRESS to getString(getColumnIndexOrThrow(MapContract.COLUMN_ADDRESS)),
                    MapContract.COLUMN_CATEGORY to getString(getColumnIndexOrThrow(MapContract.COLUMN_CATEGORY))
                )
                dataList.add(data)
            }
            close()
        }

        db.close()
        return dataList
    }
}

