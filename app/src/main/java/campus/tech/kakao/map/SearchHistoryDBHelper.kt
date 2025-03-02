package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class SearchHistoryDBHelper(context: Context) : SQLiteOpenHelper(context, "SearchHistory.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        createSearchHistoryTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS SearchHistoryTable")
        onCreate(db)
    }

    private fun createSearchHistoryTable(db: SQLiteDatabase?) {
        val searchHistoryTableSQL = """
            CREATE TABLE SearchHistoryTable (
                idx INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                address TEXT NOT NULL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """
        db?.execSQL(searchHistoryTableSQL)
    }

    fun insertSearchHistory(place: Place): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", place.name)
            put("category", place.category)
            put("address", place.address)
        }
        return db.insert("SearchHistoryTable", null, values)
    }

    fun deleteSearchHistoryByName(name: String): Int {
        val db = writableDatabase
        return db.delete("SearchHistoryTable", "`name` = ?", arrayOf(name))
    }

    fun getAllSearchHistory(): MutableList<Place> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM SearchHistoryTable ORDER BY timestamp DESC", null)
        val searchHistory = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            do {
                val idx = cursor.getInt(cursor.getColumnIndexOrThrow("idx"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                searchHistory.add(Place(idx, name, category, address))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return searchHistory
    }
}
