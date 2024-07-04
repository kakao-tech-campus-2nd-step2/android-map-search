package campus.tech.kakao.map

import android.content.ContentValues
import android.provider.BaseColumns
import android.util.Log

class PlaceRepository(context: MainActivity) {
    private val dbHelper = PlaceDbHelper(context)
    private var placeList = mutableListOf<Place>()

    init {
        dbHelper.writableDatabase
    }

    fun insertPlace(place: Place) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(MyPlaceContract.Place.COLUMN_IMG, place.img)
            put(MyPlaceContract.Place.COLUMN_NAME, place.name)
            put(MyPlaceContract.Place.COLUMN_CATEGORY, place.category)
            put(MyPlaceContract.Place.COLUMN_LOCATION, place.location)
        }

        db.insert(MyPlaceContract.Place.TABLE_NAME, null, values)
    }

    fun insertLog(place: Place) {
        val db = dbHelper.writableDatabase

        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(BaseColumns._ID),
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category),
                null,
                null,
                null
            )

            if (cursor.moveToFirst()) {
                Log.d("PlaceRepository", "Place already exists: ${place.name}")
            } else {
                val values = ContentValues().apply {
                    put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                    put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                    put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                    put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category)
                }

                val newRowId = db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                if (newRowId == -1L) {
                    Log.e("PlaceRepository", "Failed to insert row for ${place.name}")
                } else {
                    Log.d("PlaceRepository", "Successfully inserted row for ${place.name}")
                }
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error inserting row: ${e.message}")
        }
    }

    fun reset() {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${MyPlaceContract.Place.TABLE_NAME}")
        //db.execSQL("DELETE FROM ${MyPlaceContract.Research.TABLE_NAME}")
    }

    fun insertInitialData() {
        for (i in 1..10) {
            val place = Place(R.drawable.cafe, "카페$i", "강원도 춘천시 퇴계동{$i}번길", "카페")
            placeList.add(place)
            insertPlace(place)
        }

        for (i in 1..15){
            val place = Place(R.drawable.hospital, "약국$i", "강원도 강릉시 남부로{$i}번길", "약국")
            placeList.add(place)
            insertPlace(place)
        }
    }

    fun returnPlaceList() = placeList

}