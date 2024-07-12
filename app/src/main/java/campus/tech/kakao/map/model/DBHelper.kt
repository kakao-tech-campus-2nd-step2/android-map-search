package campus.tech.kakao.map.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.model.Place

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     *  코틀린에서 사용하는 companion object는 클래스 내부에 포함된 정적 멤버 변수와 메서드를 선언할 때 사용
     */
    companion object {
        private const val DATABASE_NAME = "place.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "place"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
    }

    // 테이블 생성 등 초기 데이터베이스 설정
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_LOCATION TEXT NOT NULL
            )
        """
        db.execSQL(createTableQuery)
    }

    // 데이터베이스 버전이 변경될 때 실행되는 업그레이드 로직
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // 데이터 추가
    fun insertData(name: String, location: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LOCATION, location)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // 장소 조회 조회
    fun searchPlaces(query: String): List<Place> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_NAME, COLUMN_LOCATION),
            "$COLUMN_NAME LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )

        val places = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME)
            val addressIndex = cursor.getColumnIndexOrThrow(COLUMN_LOCATION)
            do {
                val name = cursor.getString(nameIndex)
                val address = cursor.getString(addressIndex)
                places.add(Place(name, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return places
    }
}