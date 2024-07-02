package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)

    fun insertPlace(place: Place) {
        val db = dbHelper.writableDatabase

        val img = when (place.category) {
            "카페" -> R.drawable.cafe
            "약국" -> R.drawable.hospital
            else -> R.drawable.location
        }

        val values = ContentValues().apply {
            put(MyPlaceContract.Place.COLUMN_IMG, img)
            put(MyPlaceContract.Place.COLUMN_NAME, place.name)
            put(MyPlaceContract.Place.COLUMN_CATEGORY, place.category)
            put(MyPlaceContract.Place.COLUMN_LOCATION, place.location)
        }

        db.insert(MyPlaceContract.Place.TABLE_NAME, null, values)
    }
    


}