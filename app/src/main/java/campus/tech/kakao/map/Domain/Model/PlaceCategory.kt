package campus.tech.kakao.map.Domain.Model

enum class PlaceCategory {
    CAFE, RESTAURANT, PHARMACY, ELSE;

    companion object {
        fun intToCategory(ordinal: Int): PlaceCategory {
            return when (ordinal) {
                0 -> CAFE
                1 -> RESTAURANT
                2 -> PHARMACY
                else -> ELSE
            }
        }

        fun categoryToString(category: PlaceCategory): String {
            return when (category) {
                CAFE -> "카페"
                PHARMACY -> "약국"
                RESTAURANT -> "식당"
                ELSE -> "기타"
            }
        }
    }
}