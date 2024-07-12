package campus.tech.kakao.map

data class SearchKeywordResponse(
    val documents: List<LocationDto>
)

data class LocationDto(
    val id: String,
    val place_name: String,
    val category_name: String,
    val address_name: String
)
