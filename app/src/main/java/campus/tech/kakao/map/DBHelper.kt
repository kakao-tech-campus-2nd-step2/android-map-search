package campus.tech.kakao.map

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val TABLE_PLACE = "places"
private const val COLUMN_TYPE = "type"
private const val COLUMN_NAME = "name"
private const val COLUMN_ADDRESS = "address"

class DBHelper(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_PLACE (" +
                    "$COLUMN_TYPE VARCHAR(30) NOT NULL," +
                    "$COLUMN_NAME VARCHAR(30) NOT NULL," +
                    "$COLUMN_ADDRESS VARCHAR(30) NOT NULL" +
                    ");"
        )

        insertPharData(db)
        insertCafeData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PLACE")
        onCreate(db)
    }

    private fun insertPharData(db: SQLiteDatabase?) {
        val type = "약국"
        val name = "약국"
        val address = "서울 강남구 대치동"

        for (i in 1..30) {
            val nameWithIndex = "$name$i"
            val addressWithIndex = "$address $i"
            db?.execSQL("INSERT INTO $TABLE_PLACE ($COLUMN_TYPE, $COLUMN_NAME, $COLUMN_ADDRESS) VALUES ('$type', '$nameWithIndex', '$addressWithIndex');")
        }
    }

    private fun insertCafeData(db: SQLiteDatabase?) {
        val type = "카페"
        val name = "카페"
        val address = "서울 성동구 성수동"

        for (i in 1..30) {
            val nameWithIndex = "$name$i"
            val addressWithIndex = "$address $i"
            db?.execSQL("INSERT INTO $TABLE_PLACE ($COLUMN_TYPE, $COLUMN_NAME, $COLUMN_ADDRESS) VALUES ('$type', '$nameWithIndex', '$addressWithIndex');")
        }
    }

    fun searchProfiles(query: String): List<Profile> {
        val profiles = mutableListOf<Profile>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_PLACE WHERE $COLUMN_NAME LIKE ? OR $COLUMN_ADDRESS LIKE ? OR $COLUMN_TYPE LIKE ?",
            arrayOf("%$query%", "%$query%", "%$query%")
        )

        if (cursor.moveToFirst()) {
            do {
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
                profiles.add(Profile(name, address, type))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return profiles
    }
}
