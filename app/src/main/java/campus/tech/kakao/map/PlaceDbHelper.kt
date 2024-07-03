package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class PlaceDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MyPlace.db"

        private const val SQL_CREATE_PLACE_TABLE =
            "CREATE TABLE ${MyPlaceContract.Place.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${MyPlaceContract.Place.COLUMN_IMG} INTEGER," +
                    "${MyPlaceContract.Place.COLUMN_NAME} TEXT," +
                    "${MyPlaceContract.Place.COLUMN_CATEGORY} TEXT," +
                    "${MyPlaceContract.Place.COLUMN_LOCATION} TEXT)"

        private const val SQL_CREATE_MYRESEARCH_TABLE =
            "CREATE TABLE ${MyPlaceContract.MyResearch.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${MyPlaceContract.MyResearch.COLUMN_NAME} TEXT," +
                "${MyPlaceContract.MyResearch.COLUMN_IMG} INTEGER," +
                "${MyPlaceContract.MyResearch.COLUMN_LOCATION} TEXT," +
                "${MyPlaceContract.MyResearch.COLUMN_CATEGORY} TEXT)"

        private const val SQL_DELETE_PLACE = "DROP TABLE IF EXISTS ${MyPlaceContract.Place.TABLE_NAME}"
        private const val SQL_DELETE_MYRESEARCH = "DROP TABLE IF EXISTS ${MyPlaceContract.MyResearch.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_PLACE_TABLE)
        db?.execSQL(SQL_CREATE_MYRESEARCH_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_PLACE)
        db?.execSQL(SQL_DELETE_MYRESEARCH)
        onCreate(db)
    }
}