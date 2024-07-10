package campus.tech.kakao.map.util

import campus.tech.kakao.map.domain.model.Place

class PlaceMapper {
    companion object{
        fun mapPlaces(places: List<Place>): List<Place> {
            return places.map { place ->
                place.copy(category_name = setCategoryName(place.category_name),
                    place_name = setPlaceName(place.place_name)
                )
            }
        }
        private fun setPlaceName(placeName: String): String {
            return if (placeName.length > 8){ placeName.take(8)+"..." } else placeName
        }
        private fun setCategoryName(categoryName: String): String {
            return categoryName.split(" ").lastOrNull() ?: ""
        }
    }
}