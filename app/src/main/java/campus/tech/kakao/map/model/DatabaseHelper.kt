package campus.tech.kakao.map.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_SEARCH_RESULTS_TABLE = ("CREATE TABLE " + Search.TABLE_NAME + "("
                + Search.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Search.COLUMN_KEYWORD + " TEXT,"
                + Search.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")")
        db.execSQL(CREATE_SEARCH_RESULTS_TABLE)

        val CREATE_PLACES_TABLE = ("CREATE TABLE " + Place.TABLE_NAME + "("
                + Place.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Place.COLUMN_NAME + " TEXT,"
                + Place.COLUMN_LOCATION + " TEXT,"
                + Place.COLUMN_CATEGORY + " TEXT" + ")"
                )
        db.execSQL(CREATE_PLACES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Search.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${Place.TABLE_NAME}")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "search.db"
    }
}