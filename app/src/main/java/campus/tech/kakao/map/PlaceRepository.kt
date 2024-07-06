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

    fun hasResearchEntries() : Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${MyPlaceContract.Research.TABLE_NAME}", null)
        return if (cursor.moveToFirst()) {
            val count = cursor.getIntOrNull(0)
            cursor.close()
            count!! > 0
        } else {
            cursor.close()
            false
        }
    }

    fun getResearchEntries(): List<Place> {
        val db = dbHelper.readableDatabase
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
        while (cursor.moveToNext()) {
            val img = cursor.getInt(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_IMG))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_NAME))
            val location = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_LOCATION))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(MyPlaceContract.Research.COLUMN_CATEGORY))
            val place = Place(img, name, location, category)
            researchList.add(place)
        }
        cursor.close()
        return researchList
    }

    fun deleteResearchEntry(place: Place) {
        val db = dbHelper.writableDatabase
        try {
            db.delete(
                MyPlaceContract.Research.TABLE_NAME,
                "${MyPlaceContract.Research.COLUMN_NAME} = ? AND ${MyPlaceContract.Research.COLUMN_IMG} = ? AND ${MyPlaceContract.Research.COLUMN_LOCATION} = ? AND ${MyPlaceContract.Research.COLUMN_CATEGORY} = ?",
                arrayOf(place.name, place.img.toString(), place.location, place.category)
            )
            Log.d("PlaceRepository", "Successfully deleted row for ${place.name}")
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error deleting row: ${e.message}")
        }
    }

}