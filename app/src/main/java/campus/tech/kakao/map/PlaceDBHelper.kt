package campus.tech.kakao.map

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaceDBHelper private constructor(context: Context) : SQLiteOpenHelper(
    context,
    PlaceDBContract.DATABASE_NAME,
    null,
    PlaceDBContract.DATABASE_VERSION,
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PlaceDBContract.PlaceEntry.CREATE_QUERY)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        p1: Int,
        p2: Int,
    ) {
        db?.execSQL(PlaceDBContract.PlaceEntry.DROP_QUERY)
        onCreate(db)
    }

    companion object {
        private var instance: PlaceDBHelper? = null

        fun getInstance(context: Context): PlaceDBHelper {
            if (instance == null) {
                instance = PlaceDBHelper(context)
            }
            return instance!!
        }
    }
}
