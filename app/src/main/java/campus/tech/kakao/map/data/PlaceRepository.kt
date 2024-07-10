package campus.tech.kakao.map.data

import campus.tech.kakao.map.model.Place

class PlaceRepository(
    private val dbHelper: PlaceDBHelper,
) {
    fun insertPlace(place: Place) {
        dbHelper.insertPlace(place)
    }

    fun clearAllPlaces() {
        dbHelper.clearAllPlaces()
    }

    fun getPlacesByCategory(category: String): List<Place> {
        return dbHelper.getPlacesByCategory(category)
    }
}
