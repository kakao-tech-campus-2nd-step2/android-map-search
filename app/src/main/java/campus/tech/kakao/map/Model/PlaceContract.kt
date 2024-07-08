package campus.tech.kakao.map.Model

import android.database.Cursor
import android.provider.BaseColumns

object PlaceContract {
    const val DATABASE_NAME = "Place.db"

    object PlaceEntry : BaseColumns {
        const val TABLE_NAME = "place"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"

        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME varchar(30) not null," +
                "$COLUMN_ADDRESS varchar(30)," +
                "$COLUMN_CATEGORY int);";

        const val SQL_DROP_TABLE = "DROP TABLE if exists $TABLE_NAME"
    }

    object FavoriteEntry : BaseColumns {
        const val TABLE_NAME = "favorite"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"

        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_NAME varchar(30) not null," +
                "$COLUMN_ADDRESS varchar(30)," +
                "$COLUMN_CATEGORY int);";

        val SQL_DROP_TABLE = "DROP TABLE if exists $TABLE_NAME"

        val SQL_DELETE_ITEM = "DELETE FROM " +
                "$TABLE_NAME " +
                "WHERE " +
                "$COLUMN_NAME = "
    }

    fun getPlaceByCursor(cursor: Cursor): Place {
        cursor.moveToFirst()
        return Place(
            cursor.getString(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_NAME)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ADDRESS)
            ),
            PlaceCategory.intToCategory(
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_CATEGORY)
                )
            )
        )
    }

    fun getPlaceListByCursor(cursor: Cursor): List<Place> {
        var result = mutableListOf<Place>()

        while (cursor.moveToNext()) {
            val place = Place(
                cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_NAME)
                ),
                cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ADDRESS)
                ),
                PlaceCategory.intToCategory(
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_CATEGORY)
                    )
                )
            )
            result.add(place)
        }
        return result
    }
}