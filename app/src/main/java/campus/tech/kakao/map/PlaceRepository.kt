package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import androidx.core.database.getIntOrNull

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)
    private var placeList = mutableListOf<Place>()

    fun insertPlace(place: Place) {
        val db = dbHelper.writableDatabase

        try {
            val values = ContentValues().apply {
                put(MyPlaceContract.Place.COLUMN_IMG, place.img)
                put(MyPlaceContract.Place.COLUMN_NAME, place.name)
                put(MyPlaceContract.Place.COLUMN_CATEGORY, place.category.category)
                put(MyPlaceContract.Place.COLUMN_LOCATION, place.location)
            }

            db.insert(MyPlaceContract.Place.TABLE_NAME, null, values)
        } finally {
            db.close()
        }
    }

    fun insertLog(place: Place) {
        val db = dbHelper.writableDatabase

        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(BaseColumns._ID),
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category),
                null,
                null,
                null
            )

            try {
                if (cursor.moveToFirst()) {
                    Log.d("PlaceRepository", "Place already exists: ${place.name}")
                } else {
                    val values = ContentValues().apply {
                        put(MyPlaceContract.Research.COLUMN_NAME, place.name)
                        put(MyPlaceContract.Research.COLUMN_IMG, place.img)
                        put(MyPlaceContract.Research.COLUMN_LOCATION, place.location)
                        put(MyPlaceContract.Research.COLUMN_CATEGORY, place.category.category)
                    }

                    val newRowId = db.insert(MyPlaceContract.Research.TABLE_NAME, null, values)
                    if (newRowId == -1L) {
                        Log.e("PlaceRepository", "Failed to insert row for ${place.name}")
                    } else {
                        Log.d("PlaceRepository", "Successfully inserted row for ${place.name}")
                    }
                }
            } finally {
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error inserting row: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun reset() {
        val db = dbHelper.writableDatabase
        try {
            db.execSQL("DELETE FROM ${MyPlaceContract.Place.TABLE_NAME}")
            //db.execSQL("DELETE FROM ${MyPlaceContract.Research.TABLE_NAME}")
        } finally {
            db.close()
        }
    }

    fun insertInitialData() {
        for (i in 1..10) {
            val place = Place(R.drawable.cafe, "카페$i", "강원도 춘천시 퇴계동{$i}번길", PlaceCategory.CAFE)
            placeList.add(place)
            insertPlace(place)
        }

        for (i in 1..15){
            val place = Place(R.drawable.hospital, "약국$i", "강원도 강릉시 남부로{$i}번길", PlaceCategory.PHARMACY)
            placeList.add(place)
            insertPlace(place)
        }
    }

    fun returnPlaceList() = placeList

    fun hasResearchEntries() : Boolean {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null)
            try {
                return if (cursor.moveToFirst()) {
                    val count = cursor.getIntOrNull(0)
                    count!! > 0
                } else {
                    false
                }
            } finally {
                cursor.close()
            }
        } finally {
            db.close()
        }
    }

    fun getResearchEntries(): List<Place> {
        val db = dbHelper.readableDatabase
        try {
            val cursor = db.query(
                MyPlaceContract.Research.TABLE_NAME,
                arrayOf(
                    MyPlaceContract.Research.COLUMN_IMG,
                    MyPlaceContract.Research.COLUMN_NAME,
                    MyPlaceContract.Research.COLUMN_LOCATION,
                    MyPlaceContract.Research.COLUMN_CATEGORY
                ),
                null, null, null, null, null
            )

            val researchList = mutableListOf<Place>()
            try {
                while (cursor.moveToNext()) {
                    val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
                    val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
                    val categoryDisplayName = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
                    val category = PlaceCategory.fromCategory(categoryDisplayName)
                    val place = Place(img, name, location, category)
                    researchList.add(place)
                }
            } finally {
                cursor.close()
            }
            return researchList
        } finally {
            db.close()
        }
    }


    fun deleteResearchEntry(place: Place) {
        val db = dbHelper.writableDatabase
        try {
            db.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category.category)
            )
            Log.d("PlaceRepository", "Successfully deleted row for ${place.name}")
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error deleting row: ${e.message}")
        } finally {
            db.close()
        }
    }

}