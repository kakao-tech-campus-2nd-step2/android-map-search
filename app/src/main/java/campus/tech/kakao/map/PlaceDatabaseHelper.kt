package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${PlaceContract.Place.TABLE_NAME} (" +
            "  ${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "  ${PlaceContract.Place.COLUMN_NAME} TEXT not null UNIQUE," +
            "  ${PlaceContract.Place.COLUMN_ADDRESS} TEXT not null," +
            "  ${PlaceContract.Place.COLUMN_CATEGORY} TEXT not null)"

private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${PlaceContract.Place.TABLE_NAME}"

class PlaceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "place.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Place.db"
    }
}