package campus.tech.kakao.map

import android.provider.BaseColumns

object PlaceDBContract {
    const val DATABASE_NAME = "place.db"
    const val DATABASE_VERSION = 1

    object PlaceEntry : BaseColumns {
        const val TABLE_NAME = "places"
        const val COLUMN_PLACE_ID = "place_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TYPE = "type"
        const val COLUMN_ADDRESS = "address"

        const val CREATE_QUERY =
            "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_PLACE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT NOT NULL," +
                "$COLUMN_TYPE TEXT NOT NULL," +
                "$COLUMN_ADDRESS TEXT NOT NULL)"

        const val DROP_QUERY = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
