package campus.tech.kakao.map.util

import campus.tech.kakao.map.domain.model.Place

class PlaceMapper {
    companion object{
        fun mapCategoryNames(places: List<Place>): List<Place> {
            return places.map { place ->
                place.copy(category_name = getLastCategoryName(place.category_name))
            }
        }

        private fun getLastCategoryName(categoryName: String): String {
            return categoryName.split(" ").lastOrNull() ?: ""
        }
    }
}