package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocationDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            val query = "CREATE TABLE ${LocationContract.TABLE_NAME} (" +
                    "${LocationContract.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${LocationContract.COLUMN_NAME} TEXT," +
                    "${LocationContract.COLUMN_ADDRESS} TEXT," +
                    "${LocationContract.COLUMN_CATEGORY} TEXT)"
            it.execSQL(query)

            // 임의의 카페 데이터 15개 생성
            for (i in 1..15) {
                val values = ContentValues()
                values.put(LocationContract.COLUMN_NAME, "카페$i")
                values.put(LocationContract.COLUMN_ADDRESS, "서울 성동구 성수동 $i")
                values.put(LocationContract.COLUMN_CATEGORY, "카페")
                db.insert(LocationContract.TABLE_NAME, null, values)
            }

            // 임의의 약국 데이터 15개 생성
            for (i in 1..15) {
                val values = ContentValues()
                values.put(LocationContract.COLUMN_NAME, "약국$i")
                values.put(LocationContract.COLUMN_ADDRESS, "서울 강남구 대치동 $i")
                values.put(LocationContract.COLUMN_CATEGORY, "약국")
                db.insert(LocationContract.TABLE_NAME, null, values)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${LocationContract.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "location.db"
        const val DATABASE_VERSION = 1
    }
}