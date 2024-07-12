package campus.tech.kakao.map.model

data class SearchFromKeywordResponse(
    val documents: List<LocationDto>
)

data class LocationDto(
    val id: String,
    val place_name: String,
    val category_group_name: String,
    val address_name: String
)
