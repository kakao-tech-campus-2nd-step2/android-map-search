package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcel
import android.os.Parcelable

private const val TABLE_PLACE = "places"
private const val COLUMN_TYPE = "type"  //
private const val COLUMN_NAME = "name"
private const val COLUMN_ADDRESS = "address"

class DBHelper(context: Context): SQLiteOpenHelper(context, "place.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_PLACE (" +
                    "$COLUMN_TYPE  varchar(30) NOT NULL," +
                    "$COLUMN_NAME  varchar(30) NOT NULL," +
                    "$COLUMN_ADDRESS  varchar(30) NOT NULL" +
                    ");"
        )

        insertPharData()
        insertCafeData()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PLACE")
        onCreate(db)
    }

    fun insertPharData() {
        val db = this.writableDatabase
        val type = "약국"
        val name = "약국"
        val address = "서울 강남구 대치동"

        for (i in 1..30) {
            val name = "$name$i"
            val address = "$address $i"
            db.execSQL("INSERT INTO $TABLE_PLACE ($COLUMN_TYPE, $COLUMN_NAME, $COLUMN_ADDRESS) VALUES ('$type', '$name', '$address');")
        }
    }

    fun insertCafeData() {
        val db = this.writableDatabase
        val type = "카페"
        val name = "카페"
        val address = "서울 성동구 성수동"

        for (i in 1..30) {
            val name = "$name$i"
            val address = "$address $i"
            db.execSQL("INSERT INTO $TABLE_PLACE ($COLUMN_TYPE, $COLUMN_NAME, $COLUMN_ADDRESS) VALUES ('$type', '$name', '$address');")
        }
    }
}

