package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class DBHelper(context: Context, version: Int) : SQLiteOpenHelper(context, "Place.db", null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE PlaceTable (
                idx INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                address TEXT NOT NULL
            )
        """
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS PlaceTable")
        onCreate(db)
    }

    fun insert(name: String, category: String, address: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("category", category)
        values.put("address", address)
        return db.insert("PlaceTable", null, values)
    }
    data class Place(val idx: Int, val name: String, val category: String, val address: String)
}
