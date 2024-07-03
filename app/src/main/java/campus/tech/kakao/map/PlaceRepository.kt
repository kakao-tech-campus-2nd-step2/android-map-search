package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)
    private var placeList = mutableListOf<Place>()
    private var cafeList = mutableListOf<Place>()
    private var pharmacyList = mutableListOf<Place>()
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

    fun reset() {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${MyPlaceContract.Place.TABLE_NAME}")
    }

    fun insertInitialData() {
        for (i in 1..10) {
            val place = Place(R.drawable.cafe, "카페$i", "강원도 춘천시 퇴계동{$i}번길", "카페")
            placeList.add(place)
            cafeList.add(place)
            insertPlace(place)
        }

        for (i in 1..15){
            val place = Place(R.drawable.hospital, "약국$i", "강원도 강릉시 남부로{$i}번길", "약국")
            placeList.add(place)
            pharmacyList.add(place)
            insertPlace(place)
        }
    }

    fun returnPlaceList() = placeList
    fun returnCafeList() = cafeList
    fun returnPharmacyList() = pharmacyList


}