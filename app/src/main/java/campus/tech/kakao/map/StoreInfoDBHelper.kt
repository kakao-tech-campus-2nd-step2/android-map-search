package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_STORE_INFO_CREATE_ENTRIES =
    "CREATE TABLE ${StoreInfoEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${StoreInfoEntry.STORE_NAME} TEXT," +
            "${StoreInfoEntry.STORE_LOCATION} TEXT," +
            "${StoreInfoEntry.STORE_CATEGORY} TEXT)"

private const val SQL_SEARCH_WORD_CREATE_ENTRIES =
    "CREATE TABLE ${SearchWordEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${SearchWordEntry.SEARCH_WORD} TEXT UNIQUE)"

private const val SQL_STORE_INFO_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${StoreInfoEntry.TABLE_NAME}"
private const val SQL_SEARCH_WORD_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${StoreInfoEntry.TABLE_NAME}"

class StoreInfoDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "StoreInfo.db"
        const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_STORE_INFO_CREATE_ENTRIES)
        db.execSQL(SQL_SEARCH_WORD_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_STORE_INFO_DELETE_ENTRIES)
        db.execSQL(SQL_SEARCH_WORD_DELETE_ENTRIES)
        onCreate(db)
    }
}