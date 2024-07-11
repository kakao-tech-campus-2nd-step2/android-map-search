package campus.tech.kakao.map.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaceDBHelper(context: Context) : SQLiteOpenHelper(context, "placedb", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PlaceContract.PlaceEntry.CREATE_QUERY)
        db?.execSQL(PlaceContract.SavePlaceEntry.CREATE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(PlaceContract.PlaceEntry.DROP_QUERY)
        db?.execSQL(PlaceContract.SavePlaceEntry.DROP_QUERY)
        onCreate(db)
    }

}