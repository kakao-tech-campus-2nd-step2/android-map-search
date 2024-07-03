package campus.tech.kakao.map

import android.content.ContentValues
import android.content.Context

class PlaceRepository(context: Context) {
    private val dbHelper = PlaceDbHelper(context)
    private val cafeList = mutableListOf<Place>()
    private val pharmacyList = mutableListOf<Place>()
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

    fun insertInitialData() {
        for (i in 1..10) {
            val place = Place(R.drawable.cafe, "카페$i", "강원도 춘천시 퇴계동{$i}번길", "cafe")
            cafeList.add(place)
            insertPlace(place)
        }

        for (i in 1..15){
            val place = Place(R.drawable.hospital, "약국$i", "강원도 강릉시 남부로{$i}번길", "pharmacy")
            pharmacyList.add(place)
            insertPlace(place)
        }
    }

    fun returnCafeList() = cafeList
    fun returnPharmacyList() = pharmacyList

}