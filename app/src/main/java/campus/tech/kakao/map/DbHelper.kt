package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "location.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 테이블 생성
        val createCafeTable = """
            CREATE TABLE ${MapContract.TABLE_CAFE} (
                ${MapContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${MapContract.COLUMN_NAME} TEXT,
                ${MapContract.COLUMN_ADDRESS} TEXT,
                ${MapContract.COLUMN_CATEGORY} TEXT
            )
        """.trimIndent()

        val createPharmacyTable = """
            CREATE TABLE ${MapContract.TABLE_PHARMACY} (
                ${MapContract.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${MapContract.COLUMN_NAME} TEXT,
                ${MapContract.COLUMN_ADDRESS} TEXT,
                ${MapContract.COLUMN_CATEGORY} TEXT
            )
        """.trimIndent()

        db.execSQL(createCafeTable)
        db.execSQL(createPharmacyTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_CAFE}")
        db.execSQL("DROP TABLE IF EXISTS ${MapContract.TABLE_PHARMACY}")
        onCreate(db)
    }

    fun insertData() {
        val db = writableDatabase

        for (i in 1..10) {
            val insertCafe = """
                INSERT INTO ${MapContract.TABLE_CAFE} (${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY})
                VALUES ('카페$i', '서울 성동구 성수동 $i', '카페')
            """.trimIndent()
            db.execSQL(insertCafe)
        }

        for (i in 1..10) {
            val insertPharmacy = """
                INSERT INTO ${MapContract.TABLE_PHARMACY} (${MapContract.COLUMN_NAME}, ${MapContract.COLUMN_ADDRESS}, ${MapContract.COLUMN_CATEGORY})
                VALUES ('약국$i', '서울 강남구 대치동 $i', '약국')
            """.trimIndent()
            db.execSQL(insertPharmacy)
        }

        db.close()
    }
}