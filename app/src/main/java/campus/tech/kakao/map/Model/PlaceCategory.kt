package campus.tech.kakao.map.Model

enum class PlaceCategory {
    CAFE, RESTAURANT, PHARMACY, ELSE;

    companion object {
        fun intToCategory(ordinal: Int): PlaceCategory {
            return when (ordinal) {
                0 -> PlaceCategory.CAFE
                1 -> PlaceCategory.RESTAURANT
                2 -> PlaceCategory.PHARMACY
                else -> PlaceCategory.ELSE
            }
        }

        fun categoryToString(category: PlaceCategory): String {
            return when (category) {
                PlaceCategory.CAFE -> "카페"
                PlaceCategory.PHARMACY -> "약국"
                PlaceCategory.RESTAURANT -> "식당"
                PlaceCategory.ELSE -> "기타"
            }
        }
    }
}